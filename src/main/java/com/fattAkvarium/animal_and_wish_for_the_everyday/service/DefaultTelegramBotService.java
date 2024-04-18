package com.fattAkvarium.animal_and_wish_for_the_everyday.service;

import com.fattAkvarium.animal_and_wish_for_the_everyday.model.AnimalImage;
import com.fattAkvarium.animal_and_wish_for_the_everyday.model.User;
import com.fattAkvarium.animal_and_wish_for_the_everyday.model.Wish;
import com.fattAkvarium.animal_and_wish_for_the_everyday.repository.*;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.fattAkvarium.animal_and_wish_for_the_everyday.service.Smiles.*;

/**
 * Реализация TelegramBotService
 */
@Slf4j
@Service
public class DefaultTelegramBotService implements TelegramBotService {

    /**
     * Репозиторий таблицы 'animal_Image'
     */
    private final AnimalImageRepository animalImageRepository;

    /**
     * Репозиторий таблицы 'users'
     */
    private final UserRepository userRepository;

    /**
     * Класс из библиотеки telegram для отправки сообщений
     */
    private final AbsSender absSender;

    /**
     * Репозиторий таблицы 'wish'
     */
    private final WishRepository wishRepository;

    /**
     * количество изображений в таблице 'animal_image'
     */
    private final static int NUMBERS_OF_IMAGES = 116;

    /**
     * количество изображений в таблице 'wish'
     */
    private final static int NUMBERS_OF_WISHES = 50;

    private SendMessage message = new SendMessage();

    public DefaultTelegramBotService(AnimalImageRepository animalImageRepository, UserRepository userRepository, AbsSender absSender, WishRepository wishRepository) {
        this.animalImageRepository = animalImageRepository;
        this.userRepository = userRepository;
        this.absSender = absSender;
        this.wishRepository = wishRepository;
        try {
            this.absSender.execute(new SetMyCommands(commandList(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException exception) {
            log.error("Error setting bot is command list: " + exception.getMessage());
        }
    }

    public List<BotCommand> commandList() {
        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "Регистрация в боте и получение приветственного сообщения"));
        commandList.add(new BotCommand("/deletedata", "Удаление своих данных из бота"));
        return commandList;
    }

    public void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            Long chatId = message.getChatId();
            Chat chat = message.getChat();

            User user = new User(chatId, chat.getFirstName(), chat.getUserName(), new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("User saved: " + user);
        }
    }

    public void startCommandReceived(long chatId, String firstName) {
        String answer = EmojiParser.parseToUnicode("Привет " + firstName + SHIT_SMILE);
        log.info("Replied to user " + firstName + " chatId " + chatId);
        sendMessage(chatId, answer);
    }

    public void sendMessage(long chatId, String textToSend) {
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(createKeyboard());
        executeMessage(message);
    }

    public void sendImage(long chatId, String animal) {
        switch (animal) {
            case ANGEL_SMILE + CAT_SMILE, DEMON_SMILE + CAT_SMILE, "ANGEL_CAT_BUTTON", "DEMON_CAT_BUTTON" ->
                    sendImageFile(getCatsImageName(), chatId);
            case ANGEL_SMILE + DOG_SMILE, DEMON_SMILE + DOG_SMILE, "ANGEL_DOG_BUTTON", "DEMON_DOG_BUTTON" ->
                    sendImageFile(getDogsImageName(), chatId);
        }
    }

    public void choiceWish(long chatId) {
        message.setChatId(String.valueOf(chatId));
        message.setText(EmojiParser.parseToUnicode("Выбирай Добрые " + ANGEL_SMILE + "или Злые " +
                DEMON_SMILE + " пожелания " + "которые будут сопровождать Котики " + CAT_SMILE + " или Пёсики " +
                DOG_SMILE));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var angelCatButton = new InlineKeyboardButton();
        angelCatButton.setText(EmojiParser.parseToUnicode(ANGEL_SMILE + CAT_SMILE));//ангел кот
        angelCatButton.setCallbackData("ANGEL_CAT_BUTTON");

        var angelDogButton = new InlineKeyboardButton();
        angelDogButton.setText(EmojiParser.parseToUnicode(ANGEL_SMILE + DOG_SMILE));//ангел пес
        angelDogButton.setCallbackData("ANGEL_DOG_BUTTON");

        var demonCatButton = new InlineKeyboardButton();
        demonCatButton.setText(EmojiParser.parseToUnicode(DEMON_SMILE + CAT_SMILE));//демон кот
        demonCatButton.setCallbackData("DEMON_CAT_BUTTON");

        var demonDogButton = new InlineKeyboardButton();
        demonDogButton.setText(EmojiParser.parseToUnicode(DEMON_SMILE + DOG_SMILE));//демон пес
        demonDogButton.setCallbackData("DEMON_DOG_BUTTON");

        rowInLine.add(angelCatButton);
        rowInLine.add(angelDogButton);
        rowInLine.add(demonCatButton);
        rowInLine.add(demonDogButton);

        rowsInLine.add(rowInLine);
        keyboardMarkup.setKeyboard(rowsInLine);
        message.setReplyMarkup(keyboardMarkup);
        executeMessage(message);
    }

    public String sendWishes(String callBackData) {
       switch (callBackData) {
           case "ANGEL_CAT_BUTTON", "ANGEL_DOG_BUTTON",
                   ANGEL_SMILE + CAT_SMILE, ANGEL_SMILE + DOG_SMILE -> {
               return getGoodWishesText();
           }
           case "DEMON_CAT_BUTTON", "DEMON_DOG_BUTTON",
                   DEMON_SMILE + CAT_SMILE, DEMON_SMILE + DOG_SMILE -> {
               return getVeryBadWishesText();
           }
           default -> {
               return "Неизвестная команда";
           }
       }
    }

    public void deleteUserData(long chatId) {
        userRepository.deleteById(chatId);
    }


    /**
     * Метод для отправки сообщения через absSender
     * @param message отправляемое сообщение
     */
    private void executeMessage(SendMessage message ) {
        try {
            absSender.execute(message);
        } catch (TelegramApiException exception) {
            log.error("Error occurred: " + exception.getMessage());
        }
    }

    /**
     * Метод для отправки изображения через absSender
     * @param photo отправляемое изображение
     */
    private void executePhoto(SendPhoto photo) {
        try {
            absSender.execute(photo);
        } catch (TelegramApiException exception) {
            log.error("Error occurred: " + exception.getMessage());
        }
    }

    /**
     * Метод для получения изображения котиков
     * @return путь до изображения с котиками
     */
    private String getCatsImageName() {
        String fileName = "";
        Optional<AnimalImage> animalImage = animalImageRepository.findById(randomNumber(NUMBERS_OF_IMAGES));
        if (animalImage.isPresent()) {
            fileName = animalImage.get().getCatsImage();
        } else {
            getErrorForImage();
        }
        return fileName;
    }

    /**
     * Метод для получения изображения пёсиков
     * @return путь до изображения с пёсиками
     */
    private String getDogsImageName() {
        String fileName = "";
        Optional<AnimalImage> animalImage = animalImageRepository.findById(randomNumber(NUMBERS_OF_IMAGES));
        if (animalImage.isPresent()) {
            fileName = animalImage.get().getDogsImage();
        } else {
            getErrorForImage();
        }
        return fileName;
    }

    /**
     * Метод принимающий имя изображения для отправки пользователю
      * @param animalFileName путь к файлу изображения
     * @param chatId chat id пользователя
     */
    private void sendImageFile(String animalFileName, long chatId) {
        try {
            File image = ResourceUtils.getFile(animalFileName);
            SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId), new InputFile(image));
            executePhoto(sendPhoto);
        } catch (FileNotFoundException exception) {
            log.error(exception.getMessage());
        }
    }

    /**
     * Генерация рандомного числа для случайных изображений и пожеланий.
     * @param max
     * @return
     */
    private int randomNumber(int max) {
        max -= 1;
        return (int) (Math.random() * ++max) + 1;
    }

    /**
     * Метод создания клавиатуры со смайликами.
     * @return клавиатура (класс из библиотеки telegram)
     */
    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(EmojiParser.parseToUnicode(ANGEL_SMILE + CAT_SMILE));//ангел коты
        keyboardRow.add(EmojiParser.parseToUnicode(ANGEL_SMILE + DOG_SMILE));//ангел собаки
        keyboardRow.add(EmojiParser.parseToUnicode(DEMON_SMILE + CAT_SMILE));//демон коты
        keyboardRow.add(EmojiParser.parseToUnicode(DEMON_SMILE + DOG_SMILE));//демон собаки

        keyboardRows.add(keyboardRow);
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    /**
     * Возвращает доброе пожелание из таблицы 'wish'
     * @return доброе пожелание
     */
    private String getGoodWishesText() {
        String wishText = "";
        Optional<Wish> wish = wishRepository.findById(randomNumber(NUMBERS_OF_WISHES));
        if (wish.isPresent()) {
            wishText = wish.get().getGoodWishes();
        } else {
            log.error("Wish not found");
            throw new NoSuchElementException();
        }
        return wishText;
    }

    /**
     * Возвращает плохое пожелание из таблицы 'wish'
     * @return плохое пожелание
     */
    private String getVeryBadWishesText() {
        String wishText = "";
        Optional<Wish> wish = wishRepository.findById(randomNumber(NUMBERS_OF_WISHES));
        if (wish.isPresent()) {
            wishText = wish.get().getVeryBadWishes();
        } else {
            log.error("Wish not found");
            throw new NoSuchElementException();
        }
        return wishText;
    }

    /**
     * Метод записывающий в log информацию об ошибке, если изображения не существует
     * А так же кидает исключение NoSuchElementException
     */
    private void getErrorForImage() {
        log.error("Image not found");
        throw new NoSuchElementException();
    }
}
