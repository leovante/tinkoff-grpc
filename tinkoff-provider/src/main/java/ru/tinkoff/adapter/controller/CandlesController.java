package ru.tinkoff.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ru.tinkoff.adapter.client.moexIss.payload.Row;
import ru.tinkoff.adapter.dto.RequestParamSecuritiesCandles;
import ru.tinkoff.adapter.service.candle.CandleProducerService;

import java.time.LocalDate;

@RestController
@Validated
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CandlesController {

    private final static String FORMAT_DATE = "yyyy-MM-dd";
    private final CandleProducerService candleProducerService;

    @GetMapping("/candles/{security}")
    @Operation(summary = "Получение данных баров")
    @ApiResponse(
            responseCode = "200",
            description = "Данные получены",
            content = @Content(mediaType = MediaType.APPLICATION_NDJSON_VALUE, schema = @Schema(implementation = Row.class))
    )
    public @ResponseBody Flux<Row> getCandles(@PathVariable @NotNull String security,
        @DateTimeFormat(pattern = FORMAT_DATE) @RequestParam("from") @NotNull LocalDate from) {
        return candleProducerService.fetchCandles(security, from);
    }

}
