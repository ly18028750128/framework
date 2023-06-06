package com.unkow.first.telegram;


import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;
import org.springframework.util.StringUtils;

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
        HttpsURLConnection con = null;
        try {
            final String url =
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s" + (StringUtils.hasLength(parseMode) ? "&parse_mode=" + parseMode : "");
            URL obj = new URL(String.format(url, token, chatId, URLEncoder.encode(message, "UTF8")));
            con = (HttpsURLConnection) obj.openConnection();
            // 设置请求方法和超时时间
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            // 获取响应状态码
            int responseCode = con.getResponseCode();
            return responseCode;
        } catch (Exception e) {
            return 500;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        TelegramApiSendMsgUtil.single().sendMessage("5986336559:AAHmtU24bvCLztKPwHzSjKjJ7skAjQRokgc", "-664903553", "MarkdownV2：*测试消息\n测试消息*", "MarkdownV2");

        TelegramApiSendMsgUtil.single().sendMessage("5986336559:AAHmtU24bvCLztKPwHzSjKjJ7skAjQRokgc", "-664903553", "*测试消息\n测试消息*");
    }
}


