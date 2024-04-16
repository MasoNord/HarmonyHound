package com.masonord.harmonyhound.telegram.commands;
import com.masonord.harmonyhound.exception.InvalidCommand;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CommandFactory {
    public static CommandFactory INSTANCE = new CommandFactory();
    private final LanguageUtil languageUtil;

    public CommandFactory() {
        this.languageUtil = new LanguageUtil();
    }

    public Command createCommand(Message message, String chatId) throws Exception {
        Command command;

        if (message.hasAudio() || message.hasVideo()) {
            command = new RecognizeCommand(chatId, message);
        }else if (message.getText().equals("/start")) {
            command = new StartCommand(chatId);
        }else if(languageUtil.getLanguages().containsKey(message.getText())) {
            command = new ChangeLangCommand(chatId, message.getText());
        }else {
            throw new InvalidCommand(languageUtil.getProperty("command.not.found"));
        }
        return command;
    }
}
