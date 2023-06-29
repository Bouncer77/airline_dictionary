package ru.pczver.airline_dictionary.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

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
    public void addAbbreviation(String abbreviation, String originalPhrase, String userName) {

        String sql = "CALL ui_add_abbreviation(:abbreviation, :original_phrase, :user_name)";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("abbreviation", abbreviation)
                .addValue("original_phrase", originalPhrase)
                .addValue("user_name", userName);

        jdbcTemplate.update(sql, source);
    }
}
