package com.masonord.harmonyhound.telegram;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
public class TelegramBot extends SpringWebhookBot {
    private String botPath;
    private String botUsername;
    private String botToken;
    private TelegramFacade telegramFacade;

    public TelegramBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        super(setWebhook);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
//            sendChatAction(update.getMessage().getChatId());
            return telegramFacade.handleUpdate(update);
        }catch (IllegalAccessError e) {
            return new SendMessage(update.getMessage().getChatId().toString(), "Illegal Access Error");
        }catch(Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(), e.getMessage());
        }
    }

    private void sendChatAction(long chatId) {
        SendChatAction chatAction = new SendChatAction();
        chatAction.setChatId(String.valueOf(chatId));
        chatAction.setAction(ActionType.TYPING);

        try {
            // TODO: add logging
            execute(chatAction);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
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
