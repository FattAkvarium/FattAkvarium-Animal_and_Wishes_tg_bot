package com.fattAkvarium.animal_and_wish_for_the_everyday.config;

import com.fattAkvarium.animal_and_wish_for_the_everyday.controller.TelegramBotController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * класс, инициализирующий бота.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer {

    private final TelegramBotController bot;

    /**
     * Реализация класса из библиотеки telegram
     * @throws TelegramApiException
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException exception) {
            log.error("Error occurred: " + exception.getMessage());
        }
    }
}
