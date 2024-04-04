package com.fattAkvarium.animal_and_wish_for_the_everyday.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Конфигурационный класс, где из application.properties назначаются
 * имя бота и токен для его доступа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String token;

}
