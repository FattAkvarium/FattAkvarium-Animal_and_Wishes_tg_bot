package com.fattAkvarium.animal_and_wish_for_the_everyday.repository;

import com.fattAkvarium.animal_and_wish_for_the_everyday.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с таблицей 'users'
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
