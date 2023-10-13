package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitDto;

import java.sql.Timestamp;
import java.time.Instant;

@UtilityClass
public final class EndpointHitMapper {
    public EndpointHit dtoToModel(EndpointHitDto hitDto) {
        return EndpointHit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .hitDate(hitDto.getTimestamp() == null ? Timestamp.from(Instant.now())
                        : Timestamp.valueOf(hitDto.getTimestamp()))
                .build();
    }
}
