package ru.practicum.service;

import lombok.RequiredArgsConstructor;
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
@Transactional
public class StatsServiceImpl implements StatsService {
    private final StatsStorage statsStorage;

    @Override
    public void postHit(EndpointHit endpointHit) {
        statsStorage.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime s, LocalDateTime e, List<String> uris, boolean unique) {
        Timestamp start = Timestamp.valueOf(s);
        Timestamp end = Timestamp.valueOf(e);

        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return statsStorage.findAllViewStatsUniqueId(start, end);
            }
            return statsStorage.findViewStatsUniqueIdByUris(start, end, uris);
        }
        if (uris == null || uris.isEmpty()) {
            return statsStorage.findAllViewStatsNotUniqueId(start, end);
        }
        return statsStorage.findViewStatsNotUniqueIdByUris(start, end, uris);
    }

}
