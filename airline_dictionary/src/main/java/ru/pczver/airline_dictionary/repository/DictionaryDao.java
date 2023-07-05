package ru.pczver.airline_dictionary.repository;

import ru.pczver.airline_dictionary.model.Dictionary;

public interface DictionaryDao {

    String getOriginalPhraseByAbbreviation(String abbreviation);

    void addAbbreviation(String userName, String abbreviation, String originalPhrase);

    Dictionary getDictionaryByAbbreviation(String abbreviation);

    void updateAbbreviation(String userName, String abbreviation, String originalPhrase);

    Long report(String userName, String msg);
}
