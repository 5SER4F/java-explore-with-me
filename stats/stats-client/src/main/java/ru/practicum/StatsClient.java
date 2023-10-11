package ru.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsClient {
    private static final String SERVER_URL = "http://localhost:9090";
    private static final String POST_HIT_PREFIX = "/hit";
    private static final String GET_STATS_PREFIX = "/stats";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestTemplate rest;

    public StatsClient() {
        rest = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(SERVER_URL))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public HttpStatus postHit(EndpointHitDto hit) {
        ResponseEntity<Object> response = makeAndSendRequest(HttpMethod.POST, POST_HIT_PREFIX, Collections.emptyMap(),
                hit);
        return response.getStatusCode();
    }

    public List<ViewStatsDto> getStats(@NonNull LocalDateTime start,
                                       @NonNull LocalDateTime end,
                                       @NonNull List<String> uris,
                                       boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DATE_TIME_FORMAT));
        parameters.put("end", end.format(DATE_TIME_FORMAT));
        parameters.put("uris", uris);
        parameters.put("unique", unique);
        ResponseEntity<Object> response = makeAndSendRequest(HttpMethod.GET, GET_STATS_PREFIX, parameters, null);
        if (!response.hasBody()) {
            return Collections.emptyList();
        }
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                return new ObjectMapper().readValue(response.getBody().toString(), new TypeReference<List<ViewStatsDto>>() {
                });
            } catch (JsonProcessingException e) {
                throw new BadResponseException("Некорректный ответ сервера");
            }
        }
        return Collections.emptyList();
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                          Map<String, Object> parameters,
                                                          @Nullable T body) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, defaultHeaders());
        ResponseEntity<Object> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(statsServerResponse);
    }

    private static HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

}
