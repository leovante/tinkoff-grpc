package ru.tinkoff.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.adapter.client.moexIss.payload.Row;
import ru.tinkoff.adapter.dto.RequestParamSecuritiesCandles;
import ru.tinkoff.adapter.service.candle.CandleProducerService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CandlesController {

    private final CandleProducerService candleProducerService;

    @GetMapping("/candles")
    @Operation(summary = "Получение данных баров")
    @ApiResponse(
            responseCode = "200",
            description = "Данные получены",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Row.class))
    )
    public @ResponseBody List<Row> getCandles(
            @Valid RequestParamSecuritiesCandles requestParamSecuritiesCandles
    ) {
        return candleProducerService.fetch(requestParamSecuritiesCandles);
    }

}
