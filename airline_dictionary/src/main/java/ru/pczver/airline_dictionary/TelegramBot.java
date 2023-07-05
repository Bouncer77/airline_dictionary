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
        String answer = "";

        if(update.hasMessage() && update.getMessage().hasText()) {

            String msg = update.getMessage().getText().trim();
            Chat chat = update.getMessage().getChat();
            long chatId = update.getMessage().getChatId();
            log.info("user: " + chat.getUserName() + " : command: " + msg);

            String regAdd = "^/add [А-Яа-я\\w0-9_-]{1,50} [А-Яа-я\\w\\s0-9_-]{2,256}$";
            Pattern patternAdd = Pattern.compile(regAdd);

            String regReport = "^/report [А-Яа-я\\w\\s,0-9_-]{2,256}$";
            Pattern patternReport = Pattern.compile(regReport);

            String regDelete = "^/delete [А-Яа-я\\w0-9_-]{1,50}$";
            Pattern patternDelete = Pattern.compile(regDelete);

            if (msg.equals(Command.START.getCommand()) || msg.equals(Command.START.getInlineCommand())) {
                sendMessage(chatId, startCommandReceived(update));
            } else if (patternReport.matcher(msg).matches()) {
                long code = airlineDictionaryService.report(chat.getUserName(), msg);
                sendMarkdownMessage(chatId, "Сообщение принято в поддержку, код сообщения: *" + code + "*");
            } else if (patternDelete.matcher(msg).matches()) {
                log.info("command Delete");
                String abbreviation = airlineDictionaryService.delete(chat.getUserName(), msg);
                sendMessage(chatId, abbreviation);
            } else if (msg.equals(Command.ADD.getCommand()) || msg.equals(Command.ADD.getInlineCommand())) {
                sendMarkdownMessage(chatId, Command.ADD.getMsg());
            } else if (msg.equals(Command.DELETE.getCommand()) || msg.equals(Command.DELETE.getInlineCommand())) {
                sendMessage(chatId, Command.DELETE.getMsg());
            } else if (msg.equals(Command.MARKDOWN.getCommand()) || msg.equals(Command.MARKDOWN.getInlineCommand())) {
                sendMessage(chatId, Command.MARKDOWN.getMsg());
            } else if (msg.equals(Command.REPORT.getCommand()) || msg.equals(Command.REPORT.getInlineCommand())) {
                sendMessage(chatId, Command.REPORT.getMsg());
            } else if (msg.equals(Command.HELP.getCommand()) || msg.equals(Command.HELP.getInlineCommand())) {
                sendMessage(chatId, Command.HELP.getMsg());
            } else if (msg.equals(Command.MY_INFO.getCommand()) || msg.equals(Command.MY_INFO.getInlineCommand())) {
                sendMessage(chatId, myInfo(update));
            } else if (msg.equals(Command.ABOUT.getCommand()) || msg.equals(Command.ABOUT.getInlineCommand())) {
                sendMarkdownMessage(chatId, Command.ABOUT.getMsg());

                // /add QR Quick Response
            } else if (patternAdd.matcher(msg).matches()) {
                String response = airlineDictionaryService.add(msg, chat.getUserName());
                sendMessage(chatId, response);
            } else {
                try {
                    answer = airlineDictionaryService.get(msg);
                    if (Objects.isNull(answer)) {
                        answer = "Аббревиатура не найдена";
                    }
                } catch (IOException e) {
                    answer = "Ошибка ввода";
                }
                sendMarkdownMessage(chatId, "*" + msg + "*\n" + answer);
            }
        }

    }

    private String myInfo(Update update) {
        Formatter f = new Formatter();
        return f.format(Command.MY_INFO.getMsg(), update.getMessage().getChat().toString()).toString();
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
