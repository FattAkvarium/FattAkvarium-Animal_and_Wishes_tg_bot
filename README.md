# Телеграмм Бот Animal and Wishes
***
## Основная идея ##


*Это ***Телеграмм бот*** который служит для отправки добрых/плохих пожеланий на день, сопровождающихся картинками забавных котиков/пёсиков.*
***
## Базовая реализация
1. Регистрация пользователя в БД. (chatId, firstName, userName) - Данные, которые пишутся в БД.
2. Выбор пользователем пожелания и изображения, с помощью смайликов в клавиатуре телеграмм или кнопок на приветственном сообщении.
3. Бот возвращает рандомное пожелание и изображение пользователю.
4. Удаление данных о пользователе из БД, при нажатии /deletedata в меню бота.

## Стек технологий
1. Spring Boot
2. Maven
3. Postgre SQL
4. TelegramBots(библиотека телеграмм)
5. Lombok
***
## Запуск
1. В Телеграмм перейдите в [*Bot Father*](https://t.me/BotFather)
2. Зарегистрируйте нового бота(инструкция в BotFather имеется).
3. Создайте новую Базу данных в PostgreSQL или используйте имеющуюся, где отсутствуют таблицы с именами: animal_image, users, wish.
4. Заполните файл [*properties*](https://github.com/FattAkvarium/FattAkvarium-Animal_and_Wishes_tg_bot/blob/master/src/main/resources/application.properties.origin), и удалите окончание .orig
5. Запустите бота в классе [*AnimalAndWishForTheEverydayApplication*](https://github.com/FattAkvarium/FattAkvarium-Animal_and_Wishes_tg_bot/blob/master/src/main/java/com/fattAkvarium/animal_and_wish_for_the_everyday/AnimalAndWishForTheEverydayApplication.java).
6. После запуска таблицы для БД создадутся автоматически.
7. Остановите работу бота и заполните таблицы animal_image(в колонки catsImage и dogImage соответствующие картинки в строковом формате-путь до картинки на вашем ПК) и wish(в колонки goodWishes и veryBadWishes соответствующие пожелания в строковом формате).
8. В классе [*DefaultTelegramBotService*](https://github.com/FattAkvarium/FattAkvarium-Animal_and_Wishes_tg_bot/blob/master/src/main/java/com/fattAkvarium/animal_and_wish_for_the_everyday/service/DefaultTelegramBotService.java) измените статические переменные NUMBERS_OF_IMAGES и NUMBERS_OF_WISHES на числа записей в таблице animal_image и wish, после вашего заполнения.
9. Запускаем бота, переходим в телеграм и наслаждаемся пожеланиями с веселыми картинками.
