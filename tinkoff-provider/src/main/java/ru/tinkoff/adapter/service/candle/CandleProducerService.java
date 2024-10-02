package ru.tinkoff.adapter.service.candle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import ru.tinkoff.adapter.client.moexIss.payload.Row;
import ru.tinkoff.adapter.dto.RequestParamSecuritiesCandles;
import ru.tinkoff.adapter.mapper.TinkoffToMoexCandleMapper;
import ru.tinkoff.adapter.service.moexiss.reactive.MoexIssStreamService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleProducerService implements TinkoffToMoexCandleMapper {

    private final MoexIssStreamService moexIssStreamService;
    private final CandleSharesListener candleSharesListener;

    public Flux<Row> fetchCandles(String security, @RequestParam LocalDate from) {
        return moexIssStreamService
                .fetchCandles(security, from)
//                .switchIfEmpty(candleSharesListener
//                        .handleCandles()
//                        .map(this::mapCandleToRow))
        ;
    }

}
