package ru.pczver.airline_dictionary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import ru.pczver.airline_dictionary.mapper.DictionaryRowMapper;
import ru.pczver.airline_dictionary.model.Dictionary;
import ru.pczver.airline_dictionary.repository.DictionaryDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirlineDictionaryService {

    private final DictionaryDao dictionaryDao;

    public String get(String abbreviation) throws IOException {
        return dictionaryDao.getOriginalPhraseByAbbreviation(abbreviation.toUpperCase());
    }



    public String add(String msg, String userName) {
        log.info("messageText: " + msg);
        msg = msg.trim();
        // log.info("messageText after Trim: " + messageText);
        msg = msg.substring(5); // remove - "/add "
        // log.info("messageText after substring: " + messageText);
        List<String> list = new ArrayList<>(List.of(msg.split(" ")));
        // list.forEach(System.out::println);

        String abbreviation = list.get(0).toUpperCase();
        log.info("abbreviation = " + abbreviation);


        list.remove(0);
        StringBuilder originalPhraseBuilder = new StringBuilder();
        list.forEach(w -> originalPhraseBuilder.append(w).append(" "));
        String originalPhrase = originalPhraseBuilder.toString();
        log.info("originalPhrase = " + originalPhrase);


        Dictionary dictionary = dictionaryDao.getDictionaryByAbbreviation(abbreviation);

        // Такая запись уже существует
        if (Objects.nonNull(dictionary)) {
            log.info(dictionary.toString());

            // Если запись принадлежит текущему пользователю
            if (userName.equals(dictionary.getUserName())) {
                dictionaryDao.updateAbbreviation(userName, abbreviation, originalPhrase);
                log.info("Пользователь " + userName + " обновил свю аббревиатуру " + abbreviation +
                        " на " + originalPhrase);
                return "Обновлено " + abbreviation;
            } else {
                log.info(userName + "попытался обновить запись " + abbreviation + " на " + originalPhrase +
                        ", но у данного пользователя нет прав это сделать," +
                        "так как владельце аббревиатуры является пользователь " + dictionary.getUserName());
                return "Такая аббревиатура уже добавлена, и у вас нет прав перезаписать ее";
            }
        } else {
            dictionaryDao.addAbbreviation(userName, abbreviation, originalPhrase);
            log.info("Пользователь " + userName + " добавил новую аббревиатуру " + abbreviation +
                    " - " + originalPhrase);
            return "Добавлено " + abbreviation;
        }
    }

    public long report(String userName, String msg) {
        log.info("command: report, messageText: " + msg);
        msg = msg.trim();
        msg = msg.substring(8); // Удаляем название команды и пробел - "/report "
        log.info(msg);
        return dictionaryDao.report(userName, msg);
    }

    public String delete(String userName, String msg) {
        log.info("user: " + userName + "command: delete, messageText: " + msg);
        String abbreviation = msg.substring(8); // Удаляем название команды и пробел - "/delete "
        log.info("abbreviation: " + abbreviation);

        Dictionary dictionary = null;
        try {
             dictionary = dictionaryDao.getDictionaryByAbbreviation(abbreviation);

             if (dictionary != null) {
                 log.info(dictionary.toString());
                 // Проверка прав на удаление
                 if (userName.equals(dictionary.getUserName())) {
                     return dictionaryDao.delete(userName, abbreviation);
                 } else {
                     return "У вас нет прав на удаление аббревиатуры: " + abbreviation;
                 }
             } else {
                 return abbreviation + " (не существует, удаление не требуется)";
             }

        } catch (EmptyResultDataAccessException e) {
            log.info("Аббревиатура " + abbreviation + " в бд не найдена");
            return abbreviation + " (не существует, удаление не требуется)";
        }
    }
}
