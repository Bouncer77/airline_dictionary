package ru.pczver.airline_dictionary.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.pczver.airline_dictionary.mapper.DictionaryRowMapper;
import ru.pczver.airline_dictionary.model.Dictionary;

import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class DictionaryDaoImpl implements DictionaryDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DictionaryDaoImpl(@Qualifier("JdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getOriginalPhraseByAbbreviation(String abbreviation) {

        String sql = "SELECT * FROM ui_get_original_phrase(:abbreviation)";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("abbreviation", abbreviation);

        return jdbcTemplate.queryForObject(sql, source, String.class);
    }

    @Override
    public void addAbbreviation(String userName, String abbreviation, String originalPhrase) {

        log.info("addAbbreviation");
        log.info("userName = " + userName);
        log.info("abbreviation = " + abbreviation);
        log.info("originalPhrase = " + originalPhrase);

        String sqlGetDictionaryRec = "SELECT * FROM ui_get_dictionary_by_abbreviation(:abbreviation)";

        SqlParameterSource sourceGetDictionaryRec = new MapSqlParameterSource()
                .addValue("abbreviation", abbreviation);


        Dictionary dictionary = null;
        try {
            dictionary = jdbcTemplate.queryForObject(sqlGetDictionaryRec, sourceGetDictionaryRec, new DictionaryRowMapper());
        }catch (Exception e){
            log.error(e.toString());
        }

        // Такая запись уже существует
        if (Objects.nonNull(dictionary)) {
            log.info(dictionary.toString());

            // Если запись принадлежит текущему пользователю
            if (userName.equals(dictionary.getUserName())) {
                String sql = "CALL ui_dictionary_modify(:user_name, :abbreviation, :original_phrase)";
                SqlParameterSource source = new MapSqlParameterSource()
                        .addValue("abbreviation", abbreviation.trim())
                        .addValue("original_phrase", originalPhrase.trim())
                        .addValue("user_name", userName.trim());
                jdbcTemplate.update(sql, source);

                log.info("Обновлено " + abbreviation);
            } else {
                log.info("Такая аббревиатура уже добавлена, и у вас нет прав перезаписать ее");
            }
        } else {
            String sql = "CALL ui_add_abbreviation(:abbreviation, :original_phrase, :user_name)";

            SqlParameterSource source = new MapSqlParameterSource()
                    .addValue("abbreviation", abbreviation.trim())
                    .addValue("original_phrase", originalPhrase.trim())
                    .addValue("user_name", userName.trim());

            jdbcTemplate.update(sql, source);
            log.info("Добавлено " + abbreviation);
        }
    }
}

        /*List<Dictionary> result;
        try {
            result = jdbcTemplate.query(sqlGetDictionaryRec, sourceGetDictionaryRec, new DictionaryRowMapper());
            log.info(result.toString());
        }catch (Exception e){
            log.error(e.toString());
        }*/
