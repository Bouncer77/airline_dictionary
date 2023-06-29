--------------------------------------- SEARCH_PATH ---------------------------------------
set search_path = 'air_api';

----------------------------------------- SYSTEM_DATA -----------------------------------------
do $$
declare
    l_constraint_name text;
    l_table_name text;
    l_constraint_row record;
begin

----------------------------------------- ВЕРСИОНИРОВАНИЕ -------------------------------------------------

INSERT INTO air_api.version
	(version, description)
VALUES
    ('0.0.1', 'Тестовая версия 1');

----------------------------------------- СИСТЕМНЫЕ ДАННЫЕ -------------------------------------------------

INSERT INTO air_api.dictionary
	(abbreviation, original_phrase)
OVERRIDING SYSTEM VALUE
VALUES
	('ТГО', 'Технологические графики обслуживания'),
	('ВС', 'Воздушное судно')
;

INSERT INTO air_api.length_constraint
	(constraint_id, table_name, field_name, field_length, description)
VALUES
	(1, 'dictionary', 'abbreviation', 50, 'Аббревиатура'),
	(2, 'dictionary', 'original_phrase', 256, 'Исходное словосочетание')
;

-- применение правил ограничения длины полей
FOR l_constraint_row IN (SELECT table_name, field_name, field_length FROM air_api.length_constraint) LOOP
    l_constraint_name := concat('length_constraint.', l_constraint_row.table_name, '.', l_constraint_row.field_name);
    l_table_name := concat('air_api.', l_constraint_row.table_name);
    execute format('ALTER TABLE %s DROP constraint IF EXISTS "%s"', l_table_name, l_constraint_name);
    execute format('ALTER TABLE %s ADD CONSTRAINT "%s" CHECK(char_length(cast(%s as text)) <= %s);', l_table_name, l_constraint_name, l_constraint_row.field_name, l_constraint_row.field_length);
END LOOP;

end;
$$;