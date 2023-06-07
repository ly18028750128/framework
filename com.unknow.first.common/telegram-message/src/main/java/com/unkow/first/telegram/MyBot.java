package com.unkow.first.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;


public abstract class MyBot extends TelegramLongPollingBot {

    private String botUsername;
    private String botToken;

    public MyBot(String botUsername,String botToken){
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
