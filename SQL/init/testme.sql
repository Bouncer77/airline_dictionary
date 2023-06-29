SELECT * FROM air_api.ui_get_constraint();

SELECT * FROM air_api.ui_get_original_phrase('ТГО');

SELECT * FROM air_api.ui_get_original_phrase('Тfddf');

call air_api.ui_add_abbreviation('ИА', 'Истребительная Авиация');

select * from air_api.dictionary;

select * from air_api."version";

-- ver 0.0.2

-- такой функции не должно быть
call air_api.ui_add_abbreviation('ИБА', 'истребительно-бомбардировочная авиация');

call air_api.ui_add_abbreviation('ИБА', 'истребительно-бомбардировочная авиация', 'system');

select * from air_api.dictionary;

select * from air_api."version" order by version_id desc ;

-- rollback to ver 0.0.1

select * from air_api.dictionary;

select * from air_api."version" order by version_id desc ;