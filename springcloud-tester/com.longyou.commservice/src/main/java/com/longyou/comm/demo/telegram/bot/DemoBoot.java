package com.longyou.comm.demo.telegram.bot;

import com.unkow.first.telegram.MyBot;
import lombok.extern.slf4j.Slf4j;
import org.cloud.annotation.AuthLog;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class DemoBoot extends MyBot {

    public DemoBoot() {
        super("test1", "5986336559:AAHmtU24bvCLztKPwHzSjKjJ7skAjQRokgc");
    }


    @Override
    @AuthLog(bizType = "Telegram机器人", desc = "机器人(5986336559)")
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        log.info("收到一个消息{},chat_id:{}", messageText, chatId.toString());

        int lastIdx = messageText.lastIndexOf("@");
        int idx = messageText.startsWith("/") ? 1 : 0;

        if (lastIdx < 0) {
            messageText = messageText.substring(idx);
        } else {
            messageText = messageText.substring(idx, lastIdx);
        }

        SendMessage message = SendMessage.builder().chatId(chatId.toString())
            .text(String.format("消息内容：\n*%s*\n%s\n[%s](https://app.nav.ltd/#/userStatic)", messageText, messageText, "点开查看团队信息"))
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
