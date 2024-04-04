package com.fattAkvarium.animal_and_wish_for_the_everyday.repository;

import com.fattAkvarium.animal_and_wish_for_the_everyday.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с таблицей 'wish'
 */
@Repository
public interface WishRepository extends JpaRepository<Wish, Integer> {
}
