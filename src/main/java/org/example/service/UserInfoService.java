package org.example.service;

import org.example.dao.UserInfoDao;
import org.example.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Set;

/**
 * Бизнес-логика работы с пользователями
 */
public class UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoService.class);

    /**
     * Объект для работы с таблице user_info
     */
    private final UserInfoDao userInfoDao;

    /**
     * Иньекция испольуземых объектов через конструктор
     * @param userInfoDao объект для работы с таблице user_info
     */
    public UserInfoService(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    /**
     * Создание пользователя
     * @param userInfo информация о пользователе
     */
    public void createUser(UserInfo userInfo) {

        checkNameSuspicious(userInfo.getName());

        if (!isUserExists(userInfo.getName())) {

            userInfoDao.createUser(userInfo);

            LOGGER.info("User created by user info: {}", userInfo);

        } else {

            // TODO Заменить на своё исключение
            RuntimeException exception = new RuntimeException("User already exists with name " + userInfo.getName());

            LOGGER.error("Error creating user by user info {}", userInfo, exception);

            throw exception;
        }
    }

    /**
     * Возвращает информацию о пользователе по его имени
     * @param userName имя пользователя
     * @return информация о пользователе
     */
    public UserInfo getUserInfoByName(String userName) {

        try {

            return userInfoDao.getUserByName(userName);

        } catch (EmptyResultDataAccessException e) {

            LOGGER.error("Error getting info by name {}", userName, e);

            // TODO Заменить на своё исключение
            throw new RuntimeException("User not found by name " + userName);
        }
    }

    /**
     * Удаление пользователя
     * @param userName имя пользователя
     */
    public void deleteUser(String userName) {

        if (isUserExists(userName)) {

            userInfoDao.deleteUser(userName);

            LOGGER.info("User with name {} deleted", userName);
        }
    }

    /**
     * Проверка на сущестование пользователя с именем
     * @param userName имя пользователя
     * @return true - если пользователь сущестует, иначе - false
     */
    private boolean isUserExists(String userName) {
        try {
            userInfoDao.getUserByName(userName);

            return  true;

        } catch (EmptyResultDataAccessException e) {

            return false;
        }
    }

    /**
     * Проверка на то, что имя пользователя не содержится в стоп-листе
     * @param userName имя пользователя
     */
    private void checkNameSuspicious(String userName) {

        if (Set.of("administrator", "root", "system").contains(userName)) {

            // TODO: Заменить на свое исключение
            RuntimeException exception = new RuntimeException(userName + " is unacceptable");

            LOGGER.error("Check name failed", exception);

            throw exception;
        }
    }
}
