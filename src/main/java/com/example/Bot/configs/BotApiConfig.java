package com.example.Bot.configs;



import com.example.Bot.telegram.Bot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotApiConfig {
    @Bean
    public TelegramBotsApi telegramBotsApi(Bot bot) throws TelegramApiException {
        var api= new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
        return api;
    }
}
