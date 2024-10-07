package com.masonord.harmonyhound.telegram;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
public class TelegramBot extends SpringWebhookBot {
    private String botPath;
    private String botUsername;
    @Value("${telegram.bot-token}")
    private String botToken;

    private TelegramFacade telegramFacade;

    public TelegramBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        super(setWebhook);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            SendChatAction sendChatAction = new SendChatAction();
            sendChatAction.setAction(ActionType.TYPING);
            sendChatAction.setChatId(String.valueOf(update.getMessage().getChatId()));
            execute(sendChatAction);
            return telegramFacade.handleUpdate(update);
        }catch (IllegalAccessError e) {
            return new SendMessage(update.getMessage().getChatId().toString(), "Illegal Access Error");
        }catch(Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(), e.getMessage());
        }
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
