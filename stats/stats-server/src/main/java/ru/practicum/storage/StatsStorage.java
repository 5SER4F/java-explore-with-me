package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface StatsStorage extends JpaRepository<EndpointHit, Long> {

    @Query(" SELECT new ru.practicum.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) \n " +
            " FROM EndpointHit as h \n " +
            " WHERE h.uri in (:uris) AND h.hitDate BETWEEN :start AND :end \n " +
            " GROUP BY h.app, h.uri " +
            " ORDER BY count(h.ip) DESC ")
    List<ViewStatsDto> findViewStatsUniqueIdByUris(@Param("start")
                                                   Timestamp start,
                                                   @Param("end")
                                                   Timestamp end,
                                                   @Param("uris")
                                                   Collection<String> uris);

    @Query(" SELECT new ru.practicum.dto.ViewStatsDto(h.app, h.uri, count(h.ip)) \n " +
            " FROM EndpointHit as h \n " +
            " WHERE h.uri in (:uris) AND h.hitDate BETWEEN :start AND :end \n " +
            " GROUP BY h.app, h.uri " +
            " ORDER BY count(h.ip) DESC ")
    List<ViewStatsDto> findViewStatsNotUniqueIdByUris(@Param("start")
                                                      Timestamp start,
                                                      @Param("end")
                                                      Timestamp end,
                                                      @Param("uris")
                                                      Collection<String> uris);

    @Query(" SELECT new ru.practicum.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) \n " +
            " FROM EndpointHit as h \n " +
            " WHERE h.hitDate BETWEEN :start AND :end \n " +
            " GROUP BY h.app, h.uri" +
            " ORDER BY count(h.ip) DESC ")
    List<ViewStatsDto> findAllViewStatsUniqueId(@Param("start")
                                                Timestamp start,
                                                @Param("end")
                                                Timestamp end);

    @Query(" SELECT new ru.practicum.dto.ViewStatsDto(h.app, h.uri, count(h.ip)) \n " +
            " FROM EndpointHit as h \n " +
            " WHERE h.hitDate BETWEEN :start AND :end \n " +
            " GROUP BY h.app, h.uri " +
            " ORDER BY count(h.ip) DESC ")
    List<ViewStatsDto> findAllViewStatsNotUniqueId(@Param("start")
                                                   Timestamp start,
                                                   @Param("end")
                                                   Timestamp end);
}
