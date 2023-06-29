package ru.pczver.airline_dictionary;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pczver.airline_dictionary.config.BotConfig;
import ru.pczver.airline_dictionary.config.Command;
import ru.pczver.airline_dictionary.model.CurrencyModel;
import ru.pczver.airline_dictionary.service.AirlineDictionaryService;
import ru.pczver.airline_dictionary.service.CurrencyService;

import java.io.IOException;
import java.text.ParseException;
import java.util.Formatter;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    public static final String BOT_NAME = "@airline_dictionary_bot";

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

            if (messageText.equals(Command.START.getCommand()) || messageText.equals(Command.START.getInlineCommand())) {
                sendMessage(chatId, startCommandReceived(update));

            } else if (messageText.equals(Command.ADD.getCommand()) || messageText.equals(Command.ADD.getInlineCommand())) {
                // sendMarkdownMessage(chatId, Command.ADD.getMsg());

                Formatter f = new Formatter();
                String answer = f.format(Command.ADD.getMsg(), "123321").toString();
                sendMarkdownMessage(chatId, answer);

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
    private String startCommandReceived(Update update) {
        Chat chat = update.getMessage().getChat();
        String name = chat.getFirstName();
        String lastName = chat.getLastName();
        lastName = lastName == null?"":(" " + lastName); // если существует
        Formatter f = new Formatter();
        return f.format(Command.START.getMsg(), name, lastName).toString();
    }

    // через id чата отправляет пользователю сообщение в telegram
    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {

        }
    }

    private void sendMarkdownMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.enableMarkdownV2(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {

        }
    }
}
