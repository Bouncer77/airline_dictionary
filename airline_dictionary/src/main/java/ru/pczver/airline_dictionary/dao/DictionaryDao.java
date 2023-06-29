package ru.pczver.airline_dictionary.dao;

public interface DictionaryDao {

    String getOriginalPhraseByAbbreviation(String abbreviation);

    void addAbbreviation(String abbreviation, String originalPhrase, String userName);
}
