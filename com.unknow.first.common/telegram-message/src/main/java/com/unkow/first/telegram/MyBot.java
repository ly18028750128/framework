package com.unkow.first.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public abstract class MyBot extends TelegramLongPollingBot {

    private String botUsername;
    private String botToken;

    public MyBot(String botUsername,String botToken){
        this.botUsername = botUsername;
        this.botToken = botToken;
    }
    
//    @Override
//    public void onUpdateReceived(Update update) {
//        // 处理接收到的消息
//    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
