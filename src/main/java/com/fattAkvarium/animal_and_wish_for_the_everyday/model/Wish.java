package com.fattAkvarium.animal_and_wish_for_the_everyday.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность таблицы PostgreSQL 'wish'
 */
@Entity(name = "wish")
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wish {

    /**
     * primary key таблицы animalImage с автоматическим инкрементированием.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Колонка, где хранятся добрые пожелания
     */
    private String GoodWishes;

    /**
     * Колонка, где хранятся очень плохие пожелания
     */
    private String VeryBadWishes;
}
