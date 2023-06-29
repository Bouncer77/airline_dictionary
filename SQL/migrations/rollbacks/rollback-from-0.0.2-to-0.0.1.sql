--------------------------------------- SEARCH_PATH ---------------------------------------
SET search_path = 'air_api';

----------------------------------------- ROLLBACK -----------------------------------------
DO
$$
DECLARE
    l_max_version_id_dbms_api bigint;
BEGIN

SELECT max(version_id) INTO l_max_version_id_dbms_api FROM air_api."version";

-- check version
IF (select "version" from air_api."version" v WHERE version_id = l_max_version_id_dbms_api) <> '0.0.2' then
    raise exception 'Текущая версия DBMS API (%) отличается от необходимой версии (%) для выполнения скрипта!', (select * from air_api."version" v), '0.0.2' using errcode = '00001';
END IF;

-- VERSION
        INSERT INTO air_api.version
            (version, description)
        VALUES
            ('0.0.1', 'Тестовая версия 1');

-- DDL

    ALTER TABLE air_api.dictionary DROP COLUMN IF EXISTS user_name;

    EXCEPTION
        when sqlstate '00001' then
            raise notice 'SQL ERROR: [%]: %', sqlstate, sqlerrm;
            raise notice 'Выполнение текущего скрипта прервано! Изменения не применены!';
        when others then
            raise exception '%', sqlerrm using errcode = sqlstate;
    END;
$$;