package ru.pczver.airline_dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pczver.airline_dictionary.dao.DictionaryDao;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AirlineDictionaryService {

    private final DictionaryDao dictionaryDao;

    public String get(String abbreviation) throws IOException {
        return dictionaryDao.getOriginalPhraseByAbbreviation(abbreviation);
    }
}
