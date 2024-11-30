package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.Objects;

public class MyDataCommand implements Command{
    private final static Logger LOGGER = LoggerFactory.getLogger(MyDataCommand.class);
    private final User user;
    private final LanguageUtil languageUtil;

    public MyDataCommand(User user, LanguageUtil languageUtil) {
        this.user = user;
        this.languageUtil = languageUtil;
    }

    @Override
    public BotApiMethod<?> execute() {
        StringBuilder response = new StringBuilder();
        response.append(languageUtil.getProperty("user.id"))
                .append(": ")
                .append(user.getUserId())
                .append("\n");
        if (!Objects.isNull(user.getUsername())) {
            response.append(languageUtil.getProperty("user.username"))
                    .append(": ")
                    .append(user.getUsername())
                    .append("\n");
        }
        if (!Objects.isNull(user.getFirstName())) {
            response.append(languageUtil.getProperty("user.firstname"))
                    .append(": ")
                    .append(user.getFirstName())
                    .append("\n");
        }
        if (!Objects.isNull(user.getLastName())) {
            response.append(languageUtil.getProperty("user.lastname"))
                    .append(": ")
                    .append(user.getLastName())
                    .append("\n");
        }
        response.append(languageUtil.getProperty("user.apicalls"))
                .append(": ")
                .append(user.getApiCalls())
                .append("\n");

        SendMessage sendMessage = new SendMessage(user.getUserId().toString(), response.toString());
        sendMessage.enableMarkdown(true);

        LOGGER.atInfo().setMessage("My Data Command has been executed successfully").log();

        return sendMessage;
    }
}
