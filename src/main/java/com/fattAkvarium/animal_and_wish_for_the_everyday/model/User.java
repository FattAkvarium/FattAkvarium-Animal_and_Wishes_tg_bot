package com.fattAkvarium.animal_and_wish_for_the_everyday.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Сущность таблицы PostgreSQL 'users'
 */
@Entity(name = "users")
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Столбец Primary Key с id чата пользователя(chatId в telegram)
     */
    @Id
    private Long chatId;

    /**
     * колонка, где хранится имя пользователя.
     */
    private String firstName;

    /**
     * колонка, где хранится уникальное название пользователя.
     */
    private String userName;

    /**
     * колонка, где хранится дата и время регистрации пользователя в Базе Данных.
     */
    private Timestamp registeredAt;

    /**
     * Переопределенный метод toString
     * @return строковое представление заполненного класса User
     */
    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", userName='" + userName + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
