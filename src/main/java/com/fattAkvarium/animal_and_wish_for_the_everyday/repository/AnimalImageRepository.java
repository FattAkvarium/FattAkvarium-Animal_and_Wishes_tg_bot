package com.fattAkvarium.animal_and_wish_for_the_everyday.repository;

import com.fattAkvarium.animal_and_wish_for_the_everyday.model.AnimalImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с таблицей 'animalImage'
 */
@Repository
public interface AnimalImageRepository extends JpaRepository<AnimalImage, Integer> {
}
