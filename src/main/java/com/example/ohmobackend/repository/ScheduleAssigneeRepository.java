package com.example.ohmobackend.repository;

import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.ScheduleAssignee;
import com.example.ohmobackend.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleAssigneeRepository extends JpaRepository<ScheduleAssignee, Long> {

    @Query("SELECT s FROM ScheduleAssignee s " +
            "JOIN FETCH s.memberGroup mg " +
            "JOIN FETCH mg.member " +
            "WHERE s.todo = :todo"
    )
    public List<ScheduleAssignee> findAllByTodo(@Param("todo") Todo todo);

    @Query("SELECT s FROM ScheduleAssignee s " +
            "JOIN FETCH s.memberGroup mg " +
            "JOIN FETCH mg.member " +
            "WHERE s.routine = :routine")
    public List<ScheduleAssignee> findAllByRoutine(@Param("routine") Routine routine);

    @Query("select case when count(sa) > 0 then true else false end " +
            "from ScheduleAssignee sa " +
            "where (\n" +
            "            sa.todo.id = :taskId\n" +
            "         or sa.routine.id = :taskId\n" +
            "          ) " +
            "and sa.status = false "
    )
    public boolean existsIncompleteByTask(@Param("taskId") Long taskId);
}
