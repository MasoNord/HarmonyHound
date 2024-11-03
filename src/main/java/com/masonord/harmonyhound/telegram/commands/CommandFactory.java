package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.exception.InvalidCommandException;
import com.masonord.harmonyhound.exception.UnsupportedMediaTypeException;
import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CommandFactory {
    private final LanguageUtil languageUtil;
    private final User user;
    private final Message message;
    private final String botToken;

    public CommandFactory(LanguageUtil languageUtil, User user, Message message, String botToken) {
        this.languageUtil = languageUtil;
        this.user = user;
        this.message = message;
        this.botToken = botToken;
    }

    public Command createCommand() throws Exception {
        Command command;

        if (message.hasText()) {
            if (message.getText().equals("/start")) {
                command = new StartCommand(user, languageUtil);
            }else if (message.getText().equals("/help")) {
                command = new HelpCommand(user, languageUtil);
            }else if(message.getText().equals(languageUtil.getProperty("change.language.text"))
                    || languageUtil.getLanguages().containsKey(message.getText())) {
                command = new ChangeLangCommand(user, message.getText(), languageUtil);
            }else if(message.getText().equals("/info")) {
                command = new MyDataCommand(user, languageUtil);
            }else {
                throw new InvalidCommandException(languageUtil.getProperty("command.not.found"));
            }
        }else if (message.hasVoice() || message.hasAudio() || message.hasVideo() || message.hasVideoNote()){
            command = new MediaCommand(botToken, languageUtil, message);
        }else {
            throw new UnsupportedMediaTypeException(languageUtil.getProperty("unsupported.media.type"));
        }
        return command;
    }
}
