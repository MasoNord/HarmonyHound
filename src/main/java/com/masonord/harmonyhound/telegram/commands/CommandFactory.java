package com.masonord.harmonyhound.telegram.commands;
import com.masonord.harmonyhound.exception.InvalidCommand;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.hibernate.sql.Update;

public class CommandFactory {
    public static CommandFactory INSTANCE = new CommandFactory();
    private final LanguageUtil languageUtil;

    public CommandFactory() {
        this.languageUtil = new LanguageUtil();
    }

    public Command createCommand(String name, String chatId) throws Exception {
        Command command = null;

        if (name.equals("/start")) {
            command = new StartCommand(chatId);
        }else if(languageUtil.getLanguages().containsKey(name)) {
            command = new ChangeLangCommand(chatId, name);
        }else {
            command = new PongCommand(chatId);
//            throw new InvalidCommand(languageUtil.getProperty("command.not.found"));
        }

        return command;
    }
}
