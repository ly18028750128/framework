package com.longyou.comm.demo.telegram.bot;

import com.unkow.first.telegram.MyBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class DemoBoot extends MyBot {

    public DemoBoot() {
        super("demo", "5986336559:AAHmtU24bvCLztKPwHzSjKjJ7skAjQRokgc");
    }


    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        log.info("收到一个消息{},chat_id:{}", messageText,chatId.toString());


        messageText = messageText.substring(1, messageText.lastIndexOf("@"));

        SendMessage message = SendMessage.builder().chatId(chatId.toString())
            .text(String.format("消息内容：*%s*\n%s\n[%s](http://www.example.com)", messageText, messageText, messageText))
            .parseMode(ParseMode.MARKDOWNV2).build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        } finally {
            exe.shutdown();
        }
    }
}
