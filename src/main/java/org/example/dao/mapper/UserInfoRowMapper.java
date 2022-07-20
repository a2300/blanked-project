package org.example.dao.mapper;

import org.example.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Трансляция записи из таблицы user_info в java-класс UserInfo
 *
 * Используется в {@link org.example.dao.UserInfoDao}
 */
public class UserInfoRowMapper implements RowMapper<UserInfo> {
    /**
     * Возвращает информацию о пользователе
     * @param rs запись в таблице user_info
     * @param rowNum номер записи
     * @return информация о пользователе
     * @throws SQLException если в таблице нет колонки
     */
    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserInfo.builder()
                .setName(rs.getString("name"))
                .build();
    }
}
