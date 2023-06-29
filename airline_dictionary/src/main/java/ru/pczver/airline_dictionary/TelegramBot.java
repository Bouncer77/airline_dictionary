package ru.pczver.airline_dictionary;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pczver.airline_dictionary.config.BotConfig;
import ru.pczver.airline_dictionary.model.CurrencyModel;
import ru.pczver.airline_dictionary.service.AirlineDictionaryService;
import ru.pczver.airline_dictionary.service.CurrencyService;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final AirlineDictionaryService airlineDictionaryService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        CurrencyModel currencyModel = new CurrencyModel();
        String currency = "";

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String regex = "^/add [А-Яа-я\\w0-9_-]{1,50} [А-Яа-я\\w\\s0-9_-]{2,256}$";
            Pattern pattern = Pattern.compile(regex);

            if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else if (pattern.matcher(messageText).matches()) {
                // log.info(messageText);
                airlineDictionaryService.add(messageText);
            } else {
                try {
                    currency = airlineDictionaryService.get(messageText);

                    if (Objects.isNull(currency)) {
                        currency = "Аббревиатура не найдена";
                    }

                } catch (IOException e) {
                    currency = "Ошибка ввода";
                }
                sendMessage(chatId, currency);
            }
        }

    }

    // отправляет приветствие после набора команды /start в telegram
    private void startCommandReceived(Long chatId, String name) {
        String answer = "Привет, " + name + ", добро пожаловать в словарь авиакомпаний!" + "\n" +
                "Введи аббревиатуру, словосочетание которой ты хочешь узнать!\n" +
                "Например: ТГО";
        sendMessage(chatId, answer);
    }

    // через id чата отправляет пользователю сообщение в telegram
    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {

        }
    }
}
