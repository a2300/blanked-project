package org.example.web;

import org.example.model.UserInfo;
import org.example.service.UserInfoService;
import org.example.web.dto.CreateUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * Объект для работы с информацией о пользователе
     */
    private final UserInfoService userInfoService;

    /**
     * Иньекция одних объектов в другие происходит через конструктор
     * и обеспечивается библиотеками Spring
     */
    public WebController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * Обработчик запросов на создание пользователя
     * @param createUserDto запрос на создание пользователя
     */
    @PostMapping("/users")
    public void createUser(@Valid @RequestBody CreateUserDto createUserDto) {

        LOGGER.info("Create user request received: {}", createUserDto);

        /**
         * Сохраняем пользователя, преобразуя DTO в модель
         */
        userInfoService.createUser(
                UserInfo.builder().setName(createUserDto.getName()).build()
        );
    }

    /**
     * Обработчик запросов на получение информации о пользователе
     * @param userName имя пользователя
     * @return информация о пользователе
     */
    @GetMapping("/users/{userName}")
    public UserInfo getUserInfo(@PathVariable String userName) {

        LOGGER.info("Get user info request received userName={}", userName);

        return userInfoService.getUserInfoByName(userName);
    }

    /**
     * Обработчик запросов на удаление пользователя
     * @param userName имя пользователя
     */
    @DeleteMapping("/users/{userName}")
    public void deleteUser(@PathVariable String userName) {

        LOGGER.info("Delete user info request received userName={}", userName);

        userInfoService.deleteUser(userName);
    }
}