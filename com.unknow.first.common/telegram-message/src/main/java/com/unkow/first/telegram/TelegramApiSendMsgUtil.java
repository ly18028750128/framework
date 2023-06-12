package com.unkow.first.telegram;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;

@Slf4j
public final class TelegramApiSendMsgUtil {

    private TelegramApiSendMsgUtil() {

    }

    private final static TelegramApiSendMsgUtil handler = new TelegramApiSendMsgUtil();

    public static TelegramApiSendMsgUtil single() {
        return handler;
    }

    public int sendMessage(final String token, final String chatId, final String message) {
        return sendMessage(token, chatId, message, null);
    }

    /**
     * @param token     机器人token
     * @param chatId    聊天群ID
     * @param message   消息内容
     * @param parseMode 解析方式，Markdown、MarkdownV2、html
     * @return
     */
    public int sendMessage(final String token, final String chatId, final String message, String parseMode) {

        try {
            SendMessageBuilder messageBuilder = SendMessage.builder().chatId(chatId).text(message);
            if (StringUtils.hasLength(parseMode)) {
                messageBuilder.parseMode(parseMode);
            }
            DefaultAbsSender bot = new DefaultAbsSender(new DefaultBotOptions()) {
                @Override
                public String getBotToken() {
                    return token;
                }
            };
            bot.execute(messageBuilder.build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 500;
        }
        return 200;
    }

//    public static void main(String[] args) {
//        TelegramApiSendMsgUtil.single()
//            .sendMessage("5986336559:AAHmtU24bvCLztKPwHzSjKjJ7skAjQRokgc", "-664903553", "【提现】\n"
//                + "【用户】 [jz000001.mpc](https://app.nav.ltd/#/userStatic?username=jz000001.mpc)\n"
//                + "【金额】 0.90USDT\n"
//                + "【团队】 B3", "Markdown");
//
////        TelegramApiSendMsgUtil.single().sendMessage("5986336559:AAHmtU24bvCLztKPwHzSjKjJ7skAjQRokgc", "-664903553", "*测试消息\n\n\n测试消息*");
//    }
}


