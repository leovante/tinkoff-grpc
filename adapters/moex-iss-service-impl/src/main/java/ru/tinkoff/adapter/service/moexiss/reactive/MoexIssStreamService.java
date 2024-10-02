package ru.tinkoff.adapter.service.moexiss.reactive;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;
import ru.tinkoff.adapter.client.moexIss.payload.Row;

import java.time.Duration;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class MoexIssStreamService {

    @Value("${moexIss.url}")
    private String uri;

    public Flux<Row> fetchCandles(String security, @RequestParam LocalDate from) {
        var url = String.format("/iss/engines/stock/markets/shares/securities/%s/candles", security);
        return WebClient
                .create(uri)
                .get()
                .uri(builder -> builder.path(url)
                        .queryParam("fromDate", from.toString())
                        .queryParam("interval", "1")
                        .queryParam("reverse", "false")
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_NDJSON_VALUE)
                .retrieve()
                .bodyToFlux(Row.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)));
    }

}
