package com.unkow.first.telegram;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class TelegramBotsConfig implements BeanFactoryPostProcessor {
    /**
     * Spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;
    @Bean
    public TelegramBotsApi telegramBotsApi() {

        TelegramBotsApi telegramBotsApi;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            Map<String,MyBot> myBots = beanFactory.getBeansOfType(MyBot.class);

            if(!myBots.isEmpty()){
                myBots.values().forEach(myBot -> {
                    try {
                        telegramBotsApi.registerBot(myBot);
                    } catch (TelegramApiException e) {
                        log.error("{}注册失败",myBot.getBotUsername());
                    }
                });
            }
        } catch (TelegramApiException e) {
            return null;
        }
        return telegramBotsApi;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        TelegramBotsConfig.beanFactory = configurableListableBeanFactory;
    }



}
