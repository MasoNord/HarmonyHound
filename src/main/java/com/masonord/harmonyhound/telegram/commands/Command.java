package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.exception.ExceedFileSizeLimitException;
import com.masonord.harmonyhound.exception.SongNotFoundException;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

public interface Command {
    BotApiMethod<?> execute() throws ExceedFileSizeLimitException, URISyntaxException, IOException, InterruptedException, GeneralSecurityException, SongNotFoundException;
}
