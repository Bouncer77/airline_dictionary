package ru.pczver.airline_dictionary.config;

import ru.pczver.airline_dictionary.TelegramBot;

public enum Command {

    START("/start",
            "/start" + TelegramBot.BOT_NAME,
            "Привет, %s%s, добро пожаловать в словарь авиакомпаний!\n" +
                    "Введи аббревиатуру, и я постараюсь найти, что они значат!\n" +
                    "Например: ТГО"),

    ADD("/add",
            "/add" + TelegramBot.BOT_NAME,
            "Чтобы %s добавить аббревиатуру добавьте к команде /add аббревиатуру и через пробел описание:\n" +
            "`/add QR Quick Response`\n\n" +
            "Не забывайте удалять за собой ненужные аббревиатуры командой /delete\n" +
                    "Допускается использование Markdown разметки в описании /markdown"),
    DELETE("/delete",
            "/delete" + TelegramBot.BOT_NAME,
            "Для удаления добавьте название аббревиатуры: /delete QR"),

    MARKDOWN("/markdown",
            "/markdown" + TelegramBot.BOT_NAME,
            "Примеры форматирования. Обратите внимание, что спец символы нужно 'ескейпать' обратными слэшами\\n\" +\n" +
                    "            \"\\n\" +\n" +
                    "            \"Жирный текст - \\\\*жирный текст\\\\*\\n\" +\n" +
                    "            \"Курсив - \\\\_Курсив\\\\_\\n\" +\n" +
                    "            \"Ссылка (http://example.com/) - \\\\[Ссылка](http://example.com)"),

    REPORT("/report",
            "/report" + TelegramBot.BOT_NAME,
            "Напишите сообщение разработчикам, предложите свои идеи, пожалуйтесь на аббревиатуру или работу бота в целом. Используйте команду /report <текст обращения>"),

    HELP("/help",
            "/help" + TelegramBot.BOT_NAME,
            "Для добавления аббревиатуры используйте /add\\n\" +\n" +
                    "            \"Для ее удаления /delete. Для обратной связи /report.\\n\" +\n" +
                    "            \"\\n\" +\n" +
                    "            \"Отправляйте название аббревиатуры в личном чате со мной, чтобы я попытался найти ее значение.\\n\" +\n" +
                    "            \"Помимо этого вы можете использовать inline запросы для более удобного поиска и шаринга аббревиатур (будет работать в любых чатах)"),

    MY_INFO("/myinfo",
            "/myinfo" + TelegramBot.BOT_NAME,
            "Выдает полную информацию о вас"),

    ABOUT("/about",
            "/about" + TelegramBot.BOT_NAME,
            "Автор: [zver](https://pc-zver.ru/)");

    private String command;
    private String inlineCommand;

    private String msg;


    Command(String command, String inlineCommand, String msg) {
        this.command = command;
        this.inlineCommand = inlineCommand;
        this.msg = msg;
    }

    public String getCommand() {
        return command;
    }

    public String getInlineCommand() {
        return inlineCommand;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", inlineCommand='" + inlineCommand + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
