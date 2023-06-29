package ru.pczver.airline_dictionary.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.pczver.airline_dictionary.model.Dictionary;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DictionaryRowMapper implements RowMapper<Dictionary> {
    @Override
    public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dictionary res = new Dictionary();
        res.setId(rs.getLong("dictionary_id_var"));
        res.setAbbreviation(rs.getString("abbreviation_var"));
        res.setOriginalPhrase(rs.getString("original_phrase_var"));
        res.setUserName(rs.getString("user_name_var"));
        return res;
    }
}
