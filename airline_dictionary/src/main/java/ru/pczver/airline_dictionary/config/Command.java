package ru.pczver.airline_dictionary.config;

import ru.pczver.airline_dictionary.TelegramBot;

public enum Command {

    START("/start",
            TelegramBot.BOT_NAME + "/start" ,
            "Привет, %s%s, добро пожаловать в словарь авиакомпаний!\n" +
                    "Введи аббревиатуру, и я постараюсь найти, что они значат!\n" +
                    "Например: ТГО"),

    ADD("/add",
            TelegramBot.BOT_NAME + "/add",
            "Чтобы добавить аббревиатуру добавьте к команде /add аббревиатуру и через пробел описание:\n" +
            "`/add QR Quick Response`\n\n" +
            "Не забывайте удалять за собой ненужные аббревиатуры командой /delete\n" +
                    "Допускается использование Markdown разметки в описании /markdown"),
    DELETE("/delete",
            TelegramBot.BOT_NAME + "/delete",
            "Для удаления добавьте название аббревиатуры: /delete QR"),

    MARKDOWN("/markdown",
            TelegramBot.BOT_NAME + "/markdown",
            "Примеры форматирования. Обратите внимание, что спец символы нужно 'ескейпать' обратными слэшами\n" +
                    "\n" +
                    "Жирный текст - \\*жирный текст\\*\n" +
                    "Курсив - \\_Курсив\\_\n" +
                    "Ссылка (http://example.com/) - \\[Ссылка](http://example.com)"),

    REPORT("/report",
            TelegramBot.BOT_NAME + "/report",
            "Напишите сообщение разработчикам, предложите свои идеи, пожалуйтесь на аббревиатуру или работу бота в целом. Используйте команду /report <текст обращения>"),

    HELP("/help",
            TelegramBot.BOT_NAME + "/help",
            "Для добавления аббревиатуры используйте /add\n" +
                    "Для ее удаления /delete. Для обратной связи /report.\n" +
                    "\n" +
                    "Отправляйте название аббревиатуры в личном чате со мной, чтобы я попытался найти ее значение.\n" +
                    "Помимо этого вы можете использовать inline запросы для более удобного поиска и шаринга аббревиатур (будет работать в любых чатах)"),

    MY_INFO("/myinfo",
            TelegramBot.BOT_NAME + "/myinfo",
            """
                    Выдает полную информацию о вас:
                    %s

                    """),

    ABOUT("/about",
            TelegramBot.BOT_NAME + "/about",
            "Автор: [zver](https://pc-zver.ru/)");

    private final String command;
    private final String inlineCommand;

    private final String msg;


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
