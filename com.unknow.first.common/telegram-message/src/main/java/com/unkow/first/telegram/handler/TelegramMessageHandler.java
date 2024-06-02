package com.unkow.first.telegram.handler;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramMessageHandler {

    void process(TelegramLongPollingBot bot, Update update);

}
