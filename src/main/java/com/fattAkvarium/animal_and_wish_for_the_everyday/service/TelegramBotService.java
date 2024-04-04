package com.fattAkvarium.animal_and_wish_for_the_everyday.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

/**
 * Сервис для работы бота
 */
public interface TelegramBotService {

    /**
     * Метод, где прописываются команды, которые будут находиться в меню бота.
     * @return список команд.
     */
    List<BotCommand> commandList();

    /**
     * Регистрация пользователя в базе данных 'users'
     * @param message сообщение от пользователя, которого нужно регистрировать
     */
    void registerUser(Message message);

    /**
     * Команда, выполняемая после вызова /start
     * @param chatId chat Id пользователя
     * @param firstName имя пользователя
     */
    void startCommandReceived(long chatId, String firstName);

    /**
     * метод, отвечающий за отправку сообщений пользователю
     * @param chatId chat id пользователя
     * @param textToSend текст для отправки
     */
    void sendMessage(long chatId, String textToSend);

    /**
     * метод, отвечающий за отправку изображений пользователю
     * @param chatId chat id пользователя
     * @param animal животное, которое выбрал пользователь котики/песики
     */
    void sendImage(long chatId, String animal);

    /**
     * Выбор доброго или злого пожелания для будущей отправки пользователю
     * @param chatId chat id пользователя
     */
    void choiceWish(long chatId);

    /**
     * Метод, для отправки пожелания пользователю
     * @param callBackData данные, на основании которых будет отправлено нужное пожелание
     * @return Пожелание
     */
    String sendWishes(String callBackData);

    /**
     * Удаление данных по пользователе из БД 'users' по chatId
     * @param chatId chat id пользователя
     */
    void deleteUserData(long chatId);

}
