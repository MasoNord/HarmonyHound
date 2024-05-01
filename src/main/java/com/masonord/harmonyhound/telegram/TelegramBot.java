package com.masonord.harmonyhound.telegram;

import com.masonord.harmonyhound.telegram.handlers.*;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
public class TelegramBot extends SpringWebhookBot {
    private MessageHandler messageHandler;
    private MediaHandler mediaHandler;
    private CallbackQueryHandler callbackQueryHandler;
    private String botPath;
    private String botUsername;
    private String botToken;

    public TelegramBot(SetWebhook setWebhook,
                       MessageHandler messageHandler,
                       CallbackQueryHandler callbackQueryHandler,
                       MediaHandler mediaHandler) {

        super(setWebhook);
        this.callbackQueryHandler = callbackQueryHandler;
        this.messageHandler = messageHandler;
        this.mediaHandler = mediaHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        }catch (IllegalAccessError e) {
            return new SendMessage(update.getMessage().getChatId().toString(), "Illegal Access Error");
        }catch(Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(), e.getMessage());
        }
    }
    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    private BotApiMethod<?> handleUpdate(Update update) throws Exception {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        }else {
            Message message = update.getMessage();
            if (message.hasText()) {
                return messageHandler.answerMessage(message);
            }else if (message.hasVoice() || message.hasAudio() || message.hasVideo()) {
                return mediaHandler.answerMessage(message);
            }
        }
        return null;
    }
}
