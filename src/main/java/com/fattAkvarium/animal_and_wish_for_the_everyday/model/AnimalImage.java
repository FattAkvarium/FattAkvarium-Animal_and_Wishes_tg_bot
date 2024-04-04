package com.fattAkvarium.animal_and_wish_for_the_everyday.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * модель будущей таблицы animal_image, которая будет создана на основе параметров класса при первом запуске.
 */
@Entity(name = "animalImage")
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalImage {

    /**
     * primary key таблицы animalImage с автоматическим инкрементированием.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * колонка, где хранится путь до изображения(путь до файла на ПК) с котиками
     */
    private String catsImage;

    /**
     * колонка, где хранится путь до изображения(путь до файла на ПК) с пёсиками
     */
    private String dogsImage;

    public AnimalImage(String catsImage, String dogsImage) {
        this.catsImage = catsImage;
        this.dogsImage = dogsImage;
    }
}
