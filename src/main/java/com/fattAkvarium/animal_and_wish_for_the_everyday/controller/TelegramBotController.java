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

import static com.fattAkvarium.animal_and_wish_for_the_everyday.service.Smiles.*;

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
                    registerUser(update);
                    startCommandReceived(chatId, update);
                    choiceWish(chatId);
                }
                case ANGEL_SMILE + CAT_SMILE , DEMON_SMILE + CAT_SMILE,
                        ANGEL_SMILE + DOG_SMILE, DEMON_SMILE + DOG_SMILE -> {
                    sendImage(chatId, messageText);
                    sendMessageWithWishes(chatId, messageText);
                }
                case "/deletedata" -> {
                    deleteUser(chatId);
                    sendMessage(chatId, "Ваши данные удалены из базы бота." +
                            " Для повторной регистрации нажмите /start");
                }
                default -> sendMessage(chatId, EmojiParser.parseToUnicode(DEFAULT_MSG));
            }

        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText message = new EditMessageText();

            executeEditMessageText(message, callBackData, messageId, chatId);
            sendImage(chatId, callBackData);
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

    /**
     * Метод отправляющий изображение через telegramBotService
     * @param chatId id пользователя
     * @param choiceAnimal строковое представление выбранного животного
     */
    private void sendImage(long chatId, String choiceAnimal) {
        telegramBotService.sendImage(chatId, choiceAnimal);
    }

    /**
     * Метод отправляющий сообщение с пожеланием через telegramBotService
     * @param chatId id пользователя
     * @param messageText сообщение от пользователя с выбранным пожеланием(доброе/злое)
     */
    private void sendMessageWithWishes(long chatId, String messageText) {
        telegramBotService.sendMessage(chatId, telegramBotService.sendWishes(messageText));
    }

    /**
     * Метод для удаления пользователя из БД users через telegramBotService
     * @param chatId id пользователя
     */
    private void deleteUser(long chatId) {
        telegramBotService.deleteUserData(chatId);
    }

    /**
     * Метод регистрации пользователя в БД users через telegramBotService
     * @param update данные о пользователе
     */
    private void registerUser(Update update){
        telegramBotService.registerUser(update.getMessage());
    }

    /**
     * Метод отправляющий стартовое сообщение после вызова пользователем команды /start
     * через telegramBotService
     * @param chatId id пользователя
     * @param update данные о пользователе
     */
    private void startCommandReceived(long chatId, Update update) {
        telegramBotService.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
    }

    /**
     * Выбор пожелания из БД wish через telegramBotService
     * @param chatId id пользователя
     */
    private void choiceWish(long chatId) {
        telegramBotService.choiceWish(chatId);
    }

    /**
     * Отправка сообщения пользователю через telegramBotService
     * @param chatId id пользователя
     * @param message сообщение, которое будет отправлено
     */
    private void sendMessage(long chatId, String message) {
        telegramBotService.sendMessage(chatId, message);
    }
}

