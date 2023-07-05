--------------------------------------- SEARCH_PATH ---------------------------------------
SET search_path = 'air_api';

----------------------------------------- HOTFIX -----------------------------------------
DO
$$
DECLARE
    l_max_version_id_dbms_api bigint;
BEGIN

SELECT max(version_id) INTO l_max_version_id_dbms_api FROM air_api."version";

-- check version
IF (select "version" from air_api."version" v WHERE version_id = l_max_version_id_dbms_api) <> '0.0.1' then
    raise exception 'Текущая версия DBMS API (%) отличается от необходимой версии (%) для выполнения скрипта!', (select * from air_api."version" v), '0.0.1' using errcode = '00001';
END IF;

-- VERSION
INSERT INTO air_api.version
	(version, description)
VALUES
    ('0.0.2', 'Тестовая версия 2');

-- DDL
ALTER TABLE air_api.dictionary ADD COLUMN user_name text NOT NULL DEFAULT 'system';


DROP TABLE IF EXISTS air_api.report CASCADE;

CREATE TABLE air_api.report (
    report_id numeric NOT NULL,
    user_name text NOT NULL DEFAULT 'system',
    msg text NOT NULL
);

CREATE UNIQUE INDEX report_pk ON air_api.report(report_id) ;

COMMENT ON TABLE air_api.report IS 'Отчет об ошибке';
COMMENT ON COLUMN air_api.report.report_id IS 'Идентификатор отчета';
COMMENT ON COLUMN air_api.report.user_name IS 'Пользователь, отправивщий отчет';
COMMENT ON COLUMN air_api.report.msg IS 'Текст отчета';

DROP SEQUENCE IF EXISTS air_api.report_id_sequence CASCADE;
CREATE SEQUENCE air_api.report_id_sequence
	INCREMENT BY 1
	MINVALUE 100
	MAXVALUE 9223372036854775807
	CACHE 1
	NO CYCLE;

EXCEPTION
        when sqlstate '00001' then
            raise notice 'SQL ERROR: [%]: %', sqlstate, sqlerrm;
            raise notice 'Выполнение текущего скрипта прервано! Изменения не применены!';
when others then
            raise exception '%', sqlerrm using errcode = sqlstate;
END;
$$;