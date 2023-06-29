package ru.pczver.airline_dictionary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pczver.airline_dictionary.dao.DictionaryDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirlineDictionaryService {

    private final DictionaryDao dictionaryDao;

    public String get(String abbreviation) throws IOException {
        return dictionaryDao.getOriginalPhraseByAbbreviation(abbreviation.toUpperCase());
    }

    public void add(String messageText) {
        log.info("messageText: " + messageText);
        messageText = messageText.trim();
        // log.info("messageText after Trim: " + messageText);
        messageText = messageText.substring(5); // remove - "/add "
        // log.info("messageText after substring: " + messageText);
        List<String> list = new ArrayList<>(List.of(messageText.split(" ")));
        // list.forEach(System.out::println);

        String abbreviation = list.get(0).toUpperCase();
        log.info("abbreviation = " + abbreviation);


        list.remove(0);
        StringBuilder originalPhraseBuilder = new StringBuilder();
        list.forEach(w -> originalPhraseBuilder.append(w).append(" "));
        String originalPhrase = originalPhraseBuilder.toString();
        log.info("originalPhrase = " + originalPhrase);

        dictionaryDao.addAbbreviation(abbreviation, originalPhrase);
    }
}
