package org.example.web;

import org.example.web.dto.CreateUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Обработчик web-запросов
 */
@RestController
public class WebController {
    /**
     * Средство для вывода сообщений на экран
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

    /**
     * Обработчик запросов на создание пользователя
     * @param createUserDto запрос на создание пользователя
     */
    @PostMapping("/users")
    public void createUser(@Valid @RequestBody CreateUserDto createUserDto) {

        /**
         * Получили запрос на создание пользователя,
         * пока можем только залогировать этот факт
         */
        LOGGER.info("Create user request received: {}", createUserDto);
    }
}
