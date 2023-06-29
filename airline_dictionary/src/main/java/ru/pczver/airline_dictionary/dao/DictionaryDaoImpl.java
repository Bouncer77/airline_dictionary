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

        String sql = "SELECT * FROM air_api.ui_get_original_phrase(:abbreviation)";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("abbreviation", abbreviation);

        return jdbcTemplate.queryForObject(sql, source, String.class);
    }
}
