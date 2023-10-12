package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHitMapper;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsServerController {
    private final StatsService service;

    @PostMapping("/hit")
    public HttpStatus postHit(@RequestBody
                              @Valid
                              EndpointHitDto hitDto) {
        log.info("Добавление запроса: {}", hitDto);
        service.postHit(EndpointHitMapper.dtoToModel(hitDto));
        return HttpStatus.CREATED;
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam(name = "start")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @PastOrPresent
                                                       LocalDateTime start,
                                                       @RequestParam(name = "end")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @PastOrPresent
                                                       LocalDateTime end,
                                                       @RequestParam(name = "uris", required = false)
                                                       List<String> uris,
                                                       @RequestParam(name = "unique", defaultValue = "false")
                                                       boolean unique) {
        log.info("Запрос статистики в промежутке \n start={} \n end={}", start, end);
        log.info("Уникальные пользователи={}", unique);
        log.info("Список ресурсов={}", uris);
        List<ViewStatsDto> response = service.getStats(start, end, uris, unique);
        log.info("Отправлена статистика, количество элементов={}", response.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
