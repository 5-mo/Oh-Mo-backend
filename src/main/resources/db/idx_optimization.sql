-- Schedule 테이블: 회원별 날짜 조회를 위한 복합 인덱스
CREATE INDEX idx_schedule_member_date_type
    ON schedule (member_category_id, date, schedule_type);

-- Routine 테이블: 날짜별 조회를 위한 인덱스
CREATE INDEX idx_routine_date
    ON routine (date);