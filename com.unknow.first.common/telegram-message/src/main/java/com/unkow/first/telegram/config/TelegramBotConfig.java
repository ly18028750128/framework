package com.unkow.first.telegram.config;

import com.unkow.first.telegram.handler.TelegramMessageHandler;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
@ConditionalOnProperty(value = "system.telegram.bot.enabled", havingValue = "true")
public class TelegramBotConfig {

    @Value("${system.telegram.bot.config.USERNAME:}")
    private String botUsername;
    @Value("${system.telegram.bot.config.TOKEN:}")
    private String botToken;
    @Value("${system.telegram.bot.config.PROXY_HOST:}")
    private String proxyHost;
    @Value("${system.telegram.bot.config.PROXY_PORT:0}")
    private Integer proxyPort;

    @Autowired(required = false)
    private TelegramMessageHandler messageHandler;

    @Bean(value = "telegramLongPollingBot")
    public TelegramLongPollingBot telegramLongPollingBot() {

        DefaultBotOptions botOptions = new DefaultBotOptions();
        if (proxyPort > 0) {
            botOptions.setProxyHost(proxyHost);
            botOptions.setProxyPort(proxyPort);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        }

        return new TelegramLongPollingBot(botOptions, botToken) {

            @Override
            public void onUpdateReceived(Update update) {
                if (messageHandler != null) {
                    messageHandler.process(this, update);
                }
            }

            @Override
            public void onUpdatesReceived(List<Update> updates) {
                super.onUpdatesReceived(updates);
            }

            @Override
            public String getBotUsername() {
                return botUsername;
            }

            @Override
            public void onRegister() {
                super.onRegister();
            }
        };
    }

}
