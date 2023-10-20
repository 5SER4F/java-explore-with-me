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
    @NotBlank(message = "App name must be not blank")
    private String app;

    @NotBlank(message = "Uri must be not null")
    private String uri;

    @PositiveOrZero(message = "Hits must be positive or zero")
    private Long hits;
}
