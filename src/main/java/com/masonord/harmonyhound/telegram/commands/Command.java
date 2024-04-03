package com.masonord.harmonyhound.telegram.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface Command {

    BotApiMethod<?> execute();
}
