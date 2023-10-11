package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {
    @NotBlank(message = "Клиент-сервис не указан")
    private String app;

    @NotBlank(message = "Не указан uri к, которому осуществлен запрос")
    private String uri;

    @PositiveOrZero(message = "Количество обращение не может быть меньше нуля")
    private Long hits;
}
