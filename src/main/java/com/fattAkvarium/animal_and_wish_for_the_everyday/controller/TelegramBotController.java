package com.fattAkvarium.animal_and_wish_for_the_everyday.controller;

import com.fattAkvarium.animal_and_wish_for_the_everyday.config.BotConfig;
import com.fattAkvarium.animal_and_wish_for_the_everyday.service.TelegramBotService;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * класс, наследуемый от TelegramLongPollingBot(библиотека telegram)
 * Здесь содержится логика для непосредственного взаимодействия с ботом.
 */
@Slf4j
@Controller
public class TelegramBotController extends TelegramLongPollingBot {

    /**
     * конфигурация с именем и токеном бота.
     */
    private final BotConfig botConfig;

    /**
     * строковое представление смайликов с https://emojipedia.org/
     */
    private static final String ANGEL_SMILE = "\uD83D\uDE07";

    private static final String DEMON_SMILE = "\uD83D\uDC7F";

    private static final String CAT_SMILE = "\uD83D\uDC08";

    private static final String DOG_SMILE = "\uD83D\uDC36";

    private static final String SHIT_SMILE = "\uD83D\uDCA9";

    /**
     * дефолотное сообщение, для ответов на несуществующие запросы.
     */
    private static final String DEFAULT_MSG = "Сорян, такая команда не реализована, пожалуйста" +
            " перейдите в меню и ознакомьтесь с доступными командами!" + SHIT_SMILE;

    /**
     * сервис, где реализованы необходимые для работы методы.
     */
    private final TelegramBotService telegramBotService;

    public TelegramBotController(BotConfig botConfig, @Lazy TelegramBotService telegramBotService) {
        this.botConfig = botConfig;
        this.telegramBotService = telegramBotService;
    }

    /**
     * дефолный метод из TelegramLongPollingBot, который возвращает
     * @return название бота
     */
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    /**
     * дефолный метод из TelegramLongPollingBot, который возвращает
     * @return токен для работы с ботом
     */
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    /**
     * Вызывается всякий раз, когда пользователь отправляет в бот сообщение.
     * В этом методе обрабатываем поступающие от пользователя команды.
     * @param update поступающие команды от пользователя
     */
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> {
                    telegramBotService.registerUser(update.getMessage());
                    telegramBotService.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    telegramBotService.choiceWish(chatId);
                }
                case ANGEL_SMILE + CAT_SMILE , DEMON_SMILE + CAT_SMILE,
                        ANGEL_SMILE + DOG_SMILE, DEMON_SMILE + DOG_SMILE -> {
                    telegramBotService.sendImage(chatId, messageText);
                    telegramBotService.sendMessage(chatId, telegramBotService.sendWishes(messageText));
                }
                case "/deletedata" -> {
                    telegramBotService.deleteUserData(chatId);
                    telegramBotService.sendMessage(chatId, "Ваши данные удалены из базы бота." +
                            " Для повторной регистрации нажмите /start");
                }
                default -> telegramBotService.sendMessage(chatId, EmojiParser.parseToUnicode(DEFAULT_MSG));
            }

        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText message = new EditMessageText();
            switch (callBackData) {
                case "ANGEL_CAT_BUTTON", "ANGEL_DOG_BUTTON", "DEMON_CAT_BUTTON", "DEMON_DOG_BUTTON" -> {
                    executeEditMessageText(message, callBackData, messageId, chatId);
                    telegramBotService.sendImage(chatId, callBackData);
                }
            }
        }
    }

    /**
     * Метод обновляющий сообщение, выданное пользователю, на то, которое мы назначаем.
     * После чего отправляем сообщение, если нет ошибок
     * @param message сообщение, которое будем отправлять
     * @param callBackData данные, которые пришли от пользователя
     * @param messageId message id пользователя
     * @param chatId chat id пользователя
     */
    private void executeEditMessageText(EditMessageText message, String callBackData, long messageId, long chatId) {
        message.setChatId(String.valueOf(chatId));
        message.setText(telegramBotService.sendWishes(callBackData));
        message.setMessageId((int) messageId);
        try {
            execute(message);
        } catch (TelegramApiException exception) {
            log.error("Error occurred: " + exception.getMessage());
        }
    }
}

