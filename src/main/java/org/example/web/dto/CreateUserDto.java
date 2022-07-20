package org.example.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Запрос на создание пользователя
 */

/**
 Чтобы воспользоваться DTO-классом необходим механизм десериализации -
 превращения JSON-строки вида {"name": "John Doe"} в экземпляр класса
 CreateUserDto. Класс Builder реализует шаблон Строитель,
 который принято использовать в классах моделей и DTO
 */
@JsonDeserialize(builder = CreateUserDto.Builder.class)
public class CreateUserDto {

    /**
     * Имя пользователя
     * Ключ "name" - обязательный
     * Длина - от 5 до 25 символов
     * Может содержать только символы латинского алфавита
     */
    @NotNull(message = "Key 'name' is mandatory")
    @Length(min = 5, max = 25, message = "Name length must be from 5 to 25")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters a-z and A-Z")
    private final String name;

    /**
     * Конструктор сделан закрытым, потому что объекты этого класса
     * надо порождать таким образом:
     * dto = CreateUserDto.builder().setName("John Doe").build()
     */
    private CreateUserDto(Builder builder) {
        this.name = builder.name;
    }

    public String getName() { return name; }

    /**
     * Используется при выводе сообщений на экран
     */
    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * Подсказываем механизму десериализации,
     * что методы установки полей начинаются с set
     */
    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String name;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public CreateUserDto build() { return new CreateUserDto(this); }
    }

}
