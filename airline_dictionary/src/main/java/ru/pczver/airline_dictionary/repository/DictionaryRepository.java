package ru.pczver.airline_dictionary.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.pczver.airline_dictionary.mapper.DictionaryRowMapper;
import ru.pczver.airline_dictionary.model.Dictionary;

@Repository
@Slf4j
public class DictionaryRepository implements DictionaryDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DictionaryRepository(@Qualifier("JdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
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
    public Dictionary getDictionaryByAbbreviation(String abbreviation) {

        String sqlGetDictionaryRec = "SELECT * FROM ui_get_dictionary_by_abbreviation(:abbreviation)";

        SqlParameterSource sourceGetDictionaryRec = new MapSqlParameterSource()
                .addValue("abbreviation", abbreviation);


        Dictionary dictionary = null;
        try {
            dictionary = jdbcTemplate.queryForObject(sqlGetDictionaryRec, sourceGetDictionaryRec, new DictionaryRowMapper());
        } catch (Exception e) {
            log.info("Запись не найдена");
            log.error(e.toString());
            return null;
        }

        return dictionary;
    }

    @Override
    public void updateAbbreviation(String userName, String abbreviation, String originalPhrase) {
        String sql = "CALL ui_dictionary_modify(:user_name, :abbreviation, :original_phrase)";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("abbreviation", abbreviation.trim())
                .addValue("original_phrase", originalPhrase.trim())
                .addValue("user_name", userName.trim());
        jdbcTemplate.update(sql, source);
    }

    @Override
    public Long report(String userName, String msg) {
        Long result;
        final String sql = "SELECT * FROM ui_add_report(:user_name, :msg)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("user_name", userName)
                .addValue("msg", msg);
        try {
            result = jdbcTemplate.queryForObject(sql, param, Long.class);
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }

        return result;
    }

    @Override
    public String delete(String userName, String abbreviation) {
        String result;
        final String sql = "SELECT * FROM ui_delete_abbreviation(:user_name, :abbreviation)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("user_name", userName)
                .addValue("abbreviation", abbreviation);

        try {
            result = jdbcTemplate.queryForObject(sql, param, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Не удалось удалить аббревиатуру: " + abbreviation;
        }

        return result;
    }

    @Override
    public void addAbbreviation(String userName, String abbreviation, String originalPhrase) {

        String sql = "CALL ui_add_abbreviation(:abbreviation, :original_phrase, :user_name)";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("abbreviation", abbreviation.trim())
                .addValue("original_phrase", originalPhrase.trim())
                .addValue("user_name", userName.trim());

        jdbcTemplate.update(sql, source);
        log.info("Добавлено " + abbreviation);
    }
}

        /*List<Dictionary> result;
        try {
            result = jdbcTemplate.query(sqlGetDictionaryRec, sourceGetDictionaryRec, new DictionaryRowMapper());
            log.info(result.toString());
        }catch (Exception e){
            log.error(e.toString());
        }*/
