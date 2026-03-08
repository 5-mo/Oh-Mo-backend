package com.example.ohmobackend.service.assigneeService;

import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.GroupRole;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AssigneeCommandService.updateAssigneeStatus() 의 동시성 정합성 테스트.
 *
 * 검증 시나리오:
 * - 담당자 A, B가 같은 투두에 배정되어 있을 때
 * - 두 사람이 동시에 완료 처리를 하면 투두 status가 true로 업데이트되어야 한다.
 *
 * 현재 코드(락 없음)에서는 다음 race condition이 발생할 수 있다:
 * 1. Thread A: A.status = true 저장 (미커밋)
 * 2. Thread B: B.status = true 저장 (미커밋)
 * 3. Thread A: existsIncompleteByTask() → B가 아직 미커밋이므로 false 반환 → 투두 status = false
 * 4. Thread B: existsIncompleteByTask() → A가 아직 미커밋이므로 false 반환 → 투두 status = false
 * 5. 결과: 두 담당자 모두 완료됐지만 투두 status는 false (불일치)
 */
@SpringBootTest
@ActiveProfiles("test")
class AssigneeStatusConcurrencyTest {

    @Autowired
    private AssigneeCommandService assigneeCommandService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MemberGroupRepository memberGroupRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ScheduleAssigneeRepository scheduleAssigneeRepository;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    private Member memberA;
    private Member memberB;
    private Todo todo;
    private ScheduleAssignee assigneeA;
    private ScheduleAssignee assigneeB;

    @BeforeEach
    void setUp() {
        // FK 의존 순서에 맞게 정리
        scheduleAssigneeRepository.deleteAll();
        todoRepository.deleteAll();
        scheduleRepository.deleteAll();
        memberGroupRepository.deleteAll();
        groupRepository.deleteAll();
        memberRepository.deleteAll();

        memberA = memberRepository.save(Member.builder()
                .nickname("회원A")
                .email("a@test.com")
                .password("password")
                .build());

        memberB = memberRepository.save(Member.builder()
                .nickname("회원B")
                .email("b@test.com")
                .password("password")
                .build());

        Group group = groupRepository.save(Group.builder()
                .groupName("테스트 그룹")
                .groupColor("#FFFFFF")
                .numPeople(2)
                .build());

        MemberGroup memberGroupA = memberGroupRepository.save(MemberGroup.builder()
                .member(memberA)
                .group(group)
                .nickname("A닉네임")
                .role(GroupRole.MEMBER)
                .build());

        MemberGroup memberGroupB = memberGroupRepository.save(MemberGroup.builder()
                .member(memberB)
                .group(group)
                .nickname("B닉네임")
                .role(GroupRole.MEMBER)
                .build());

        Schedule schedule = scheduleRepository.save(Schedule.builder()
                .date(LocalDate.now())
                .scheduleType(ScheduleType.TO_DO)
                .group(group)
                .createdBy(memberGroupA)
                .build());

        todo = todoRepository.save(Todo.builder()
                .schedule(schedule)
                .build());

        assigneeA = scheduleAssigneeRepository.save(ScheduleAssignee.builder()
                .memberGroup(memberGroupA)
                .todo(todo)
                .build());

        assigneeB = scheduleAssigneeRepository.save(ScheduleAssignee.builder()
                .memberGroup(memberGroupB)
                .todo(todo)
                .build());
    }

    @Test
    @DisplayName("[기준점] 순차 완료 처리 시 투두 상태가 완료로 업데이트된다")
    void 순차_완료처리_정상_동작() {
        // when: A 완료 → B 완료 (순차)
        assigneeCommandService.updateAssigneeStatus(assigneeA.getId(), memberA);
        assigneeCommandService.updateAssigneeStatus(assigneeB.getId(), memberB);

        // then
        Todo result = todoRepository.findById(todo.getId()).orElseThrow();
        assertThat(result.isStatus())
                .as("두 담당자가 순서대로 완료 처리하면 투두도 완료(true)가 되어야 한다")
                .isTrue();
    }

    @Test
    @DisplayName("[버그 재현] 두 담당자 동시 완료 처리 시 투두 상태가 미완료로 남을 수 있다")
    void 두_담당자_동시_완료처리_정합성_테스트() throws InterruptedException {
        Long assigneeAId = assigneeA.getId();
        Long assigneeBId = assigneeB.getId();

        CountDownLatch startLatch = new CountDownLatch(1); // 두 스레드 동시 출발 신호
        CountDownLatch doneLatch = new CountDownLatch(2);  // 두 스레드 종료 대기
        Throwable[] errors = new Throwable[2];

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try {
                startLatch.await(); // 출발 신호 대기
                assigneeCommandService.updateAssigneeStatus(assigneeAId, memberA);
            } catch (Throwable e) {
                errors[0] = e;
            } finally {
                doneLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                startLatch.await(); // 출발 신호 대기
                assigneeCommandService.updateAssigneeStatus(assigneeBId, memberB);
            } catch (Throwable e) {
                errors[1] = e;
            } finally {
                doneLatch.countDown();
            }
        });

        startLatch.countDown(); // 두 스레드 동시 시작
        boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).as("두 스레드가 10초 내에 완료되어야 한다").isTrue();
        assertThat(errors[0]).as("Thread A에서 예외가 발생하면 안 된다").isNull();
        assertThat(errors[1]).as("Thread B에서 예외가 발생하면 안 된다").isNull();

        // 담당자 개별 상태 검증
        ScheduleAssignee resultA = scheduleAssigneeRepository.findById(assigneeAId).orElseThrow();
        ScheduleAssignee resultB = scheduleAssigneeRepository.findById(assigneeBId).orElseThrow();
        assertThat(resultA.isStatus()).as("담당자 A의 status는 true여야 한다").isTrue();
        assertThat(resultB.isStatus()).as("담당자 B의 status는 true여야 한다").isTrue();

        // 핵심 검증: 모든 담당자가 완료 → 투두도 완료여야 함
        // 락 없는 현재 코드에서는 race condition으로 false가 될 수 있음
        Todo result = todoRepository.findById(todo.getId()).orElseThrow();
        assertThat(result.isStatus())
                .as("두 담당자 모두 완료 처리했으므로 투두 status도 true여야 한다 " +
                    "(실패 시 → race condition 버그 확인됨, Pessimistic Lock 적용 필요)")
                .isTrue();
    }
}
