-- ============================================================
-- 인덱스 최적화 스크립트
-- 배포 시마다 실행 가능 (이미 존재하는 인덱스는 건너뜀)
-- ============================================================

DROP PROCEDURE IF EXISTS create_index_if_not_exists;

DELIMITER //
CREATE PROCEDURE create_index_if_not_exists(
    IN p_table  VARCHAR(255),
    IN p_index  VARCHAR(255),
    IN p_columns TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.STATISTICS
        WHERE table_schema = DATABASE()
          AND table_name   = p_table
          AND index_name   = p_index
    ) THEN
        SET @sql = CONCAT('CREATE INDEX `', p_index, '` ON `', p_table, '` (', p_columns, ')');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END //
DELIMITER ;

-- ── Schedule
-- 그룹별 날짜 조회 (그룹 일정 목록 API 핵심 인덱스)
CALL create_index_if_not_exists('schedule', 'idx_schedule_group_date','group_id, date');

-- 그룹 내 schedule_id 조회
CALL create_index_if_not_exists('schedule', 'idx_schedule_group', 'group_id, schedule_id');

-- 회원별 날짜·타입 조회 (개인 일정 목록)
CALL create_index_if_not_exists('schedule', 'idx_schedule_member_date_type','member_category_id, date, schedule_type');

-- ── Routine
-- 날짜 기준 루틴 조회
CALL create_index_if_not_exists('routine', 'idx_routine_schedule_date','date, schedule_id');

-- schedule_id 기준 루틴 조회
CALL create_index_if_not_exists('routine', 'idx_routine_date', 'schedule_id, date');

-- ── MemberGroup
-- 그룹 + 멤버 복합 조회
CALL create_index_if_not_exists('member_group', 'idx_member_group_group_member', 'group_id, member_id');

-- 멤버 기준 소속 그룹 목록 조회
CALL create_index_if_not_exists('member_group', 'idx_member_group_member','member_id');

-- ── ScheduleAssignee
-- todo 기준 담당자 조회
CALL create_index_if_not_exists('schedule_assignee', 'idx_schedule_assignee_todo', 'todo_id');

-- routine 기준 담당자 조회
CALL create_index_if_not_exists('schedule_assignee', 'idx_schedule_assignee_routine', 'routine_id');


DROP PROCEDURE IF EXISTS create_index_if_not_exists;
