--------------------------------------- SEARCH_PATH ---------------------------------------
set search_path = 'air_api';

---------------------------------------- FUNCTIONS ----------------------------------------
DO
$$
DECLARE
    l_schemas text[] = array['air_api'];
    l_schema_name text;
    r record;
    l_max_version_id_dbms_api bigint;
BEGIN
    
SELECT max(version_id) INTO l_max_version_id_dbms_api FROM air_api."version";

-- check version
IF (select "version" from air_api."version" v WHERE version_id = l_max_version_id_dbms_api) <> '0.0.1' then
    raise exception 'Текущая версия DBMS API (%) отличается от необходимой версии (%) для выполнения скрипта!', (select * from air_api."version" v), '0.0.1' using errcode = '00001';
END IF;

-- delete all functions/procedures in schema "air_api"
foreach l_schema_name in array l_schemas loop
    -- функции & процедуры
    for r in (select oid, prokind from pg_proc where pronamespace = l_schema_name::regnamespace) loop
        execute 'drop ' || case r.prokind when 'f' then 'function' when 'p' then 'procedure' end || ' if exists ' || r.oid::regprocedure || ' cascade';
    end loop;
end loop;

CREATE OR REPLACE FUNCTION air_api.ui_get_original_phrase(v_abbreviation text)
    RETURNS text
    LANGUAGE plpgsql
AS $function$
    DECLARE
        l_original_phrase text;
    BEGIN
        SELECT original_phrase INTO l_original_phrase FROM dictionary where abbreviation = v_abbreviation LIMIT 1;
        RETURN l_original_phrase;
    END;
$function$
    SET search_path = air_api, pg_temp;

CREATE OR REPLACE FUNCTION air_api.ui_get_constraint()
    RETURNS TABLE (
        type_id_var bigint,
        name_var text,
        value_var numeric,
        description_var text)
    LANGUAGE plpgsql
AS $function$
    BEGIN
        RETURN QUERY select constraint_id, concat(table_name, '.',field_name), field_length, description from length_constraint;
    END;
$function$
    SET search_path = air_api, pg_temp;

CREATE OR REPLACE PROCEDURE air_api.ui_add_abbreviation(v_abbreviation text, v_original_phrase text)
	LANGUAGE plpgsql
AS $procedure$
	BEGIN
        INSERT INTO air_api.dictionary
	        (abbreviation, original_phrase)
        VALUES
            (v_abbreviation, v_original_phrase);
	END;
$procedure$
	SET search_path = air_api, pg_temp;

end;
$$;