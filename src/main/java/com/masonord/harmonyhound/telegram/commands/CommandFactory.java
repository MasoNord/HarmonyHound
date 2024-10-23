package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.exception.InvalidCommandException;
import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CommandFactory {
    public static CommandFactory INSTANCE = new CommandFactory();
    private final LanguageUtil languageUtil;

    public CommandFactory() {
        this.languageUtil = new LanguageUtil();
    }

    public Command createCommand(Message message, User user) throws Exception {
        Command command;
        String chatId = user.getUserId().toString();

        if (message.getText().equals("/start")) {
            command = new StartCommand(chatId);
        }else if (message.getText().equals("/help")) {
            command = new HelpCommand(chatId);
        }else if(languageUtil.getLanguages().containsKey(message.getText()) || message.getText().equals(languageUtil.getProperty("change.language.text"))) {
            command = new ChangeLangCommand(chatId, message.getText());
        }else if(message.getText().equals("/info")) {
            command = new MyDataCommand(user);
        }else {
            throw new InvalidCommandException(languageUtil.getProperty("command.not.found"));
        }
        return command;
    }
}
