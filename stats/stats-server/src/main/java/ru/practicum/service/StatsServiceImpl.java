package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.storage.StatsStorage;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsStorage statsStorage;

    @Override
    @Transactional
    public void postHit(EndpointHit endpointHit) {
        EndpointHit addedEendpointHit = statsStorage.save(endpointHit);
        log.info("Добавленно обращение к ресурсу:{} ", addedEendpointHit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime s, LocalDateTime e, List<String> uris, boolean unique) {
        Timestamp start = Timestamp.valueOf(s);
        Timestamp end = Timestamp.valueOf(e);
        List<ViewStatsDto> stats;
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                stats = statsStorage.findAllViewStatsUniqueId(start, end);
            } else {
                stats = statsStorage.findViewStatsUniqueIdByUris(start, end, uris);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                stats = statsStorage.findAllViewStatsNotUniqueId(start, end);
            } else {
                stats = statsStorage.findViewStatsNotUniqueIdByUris(start, end, uris);
            }
        }
        log.info("Сформирован списко статистики длинной = {}", stats.size());
        return stats;
    }

}
