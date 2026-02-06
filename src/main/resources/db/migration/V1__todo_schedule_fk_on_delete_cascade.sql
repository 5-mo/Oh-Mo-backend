-- 1. 기존 FK 제거
ALTER TABLE todo
DROP FOREIGN KEY FK2ox5cnp9xw5u9kib4t98owv2h;

-- 2. ON DELETE CASCADE 붙여서 재생성
ALTER TABLE todo
    ADD CONSTRAINT FK2ox5cnp9xw5u9kib4t98owv2h
        FOREIGN KEY (schedule_id)
            REFERENCES schedule(schedule_id)
            ON DELETE CASCADE;

