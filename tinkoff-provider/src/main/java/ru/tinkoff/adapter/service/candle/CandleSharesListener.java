package ru.tinkoff.adapter.service.candle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;
import ru.tinkoff.adapter.service.processor.PurchaseService;
import ru.tinkoff.piapi.contract.v1.Candle;

/**
 * Service to observe candles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CandleSharesListener {

    @Qualifier("ReplayProcessorShares")
    @Autowired
    private ReplayProcessor<Candle> replayProcessor;
    private final PurchaseService purchaseService;

    public Flux<Candle> handleCandles() {
        return replayProcessor
//                .doOnNext(purchaseService::observeCandleNoThrow)
                ;
    }

}
