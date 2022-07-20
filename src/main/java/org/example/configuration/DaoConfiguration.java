package org.example.configuration;

import org.example.dao.UserInfoDao;
import org.example.service.UserInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Создание "бинов" DAO-классов
 */
@Configuration
public class DaoConfiguration {
    @Bean
    UserInfoDao userInfoDao(NamedParameterJdbcTemplate jdbcTemplate) {
        return new UserInfoDao(jdbcTemplate);
    }

    @Bean
    UserInfoService userInfoService(UserInfoDao userInfoDao) {
        return new UserInfoService(userInfoDao);
    }
}
