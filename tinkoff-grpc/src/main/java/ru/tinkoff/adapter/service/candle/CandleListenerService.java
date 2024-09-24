package ru.tinkoff.adapter.service.candle;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.tinkoff.adapter.service.instruments.Instrument;
import ru.tinkoff.adapter.service.instruments.InstrumentService;
import ru.tinkoff.adapter.service.processor.PurchaseService;
import ru.tinkoff.adapter.service.tinkoff.ITinkoffCommonAPI;
import ru.tinkoff.piapi.contract.v1.SubscriptionStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service to observe candles
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tinkoff.listener.enabled", havingValue = "true")
@Slf4j
public class CandleListenerService {

    private final ITinkoffCommonAPI tinkoffCommonAPI;
    private final InstrumentService instrumentService;
    private final PurchaseService purchaseService;

    private void startToListen() {
        var figis = getFigis(instrumentService.getSharesByTicker());
        if (CollectionUtils.isEmpty(figis)) {
            log.warn("No figies to subscribe");
            return;
        }

        log.info("Start to listen candle events..");
        try {
            tinkoffCommonAPI.getApi().getMarketDataStreamService()
                    .newStream("candles_stream", item -> {
                        log.trace("New data in streaming api: {}", item);
                        if (item.hasCandle()) {
                            purchaseService.observeCandleNoThrow(item.getCandle());
                        } else {
                            String errSubs = item.getSubscribeCandlesResponse().getCandlesSubscriptionsList().stream()
                                    .filter(f -> f.getSubscriptionStatus() != SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)
                                    .map(i -> String.format("Subscriptions errors: %s - %s", i.getSubscriptionStatus(), i.getFigi()))
                                    .collect(Collectors.joining());
                            if (!errSubs.isBlank()) {
                                throw new RuntimeException(errSubs);
                            }

                        }
                    }, e -> {
                        log.error("An error in candles_stream, listener will be restarted", e);
//                        startToListen();
                    })
                    .subscribeCandles(figis);
        } catch (Throwable th) {
            log.error("An error in subscriber, listener will be restarted", th);
//            startToListen();
            throw th;
        }
    }


    private List<String> getFigis(Map<String, Instrument> sharesByTicker){
        return sharesByTicker.keySet().stream().toList();
    }

    @PostConstruct
    void init() {
        new Thread(this::startToListen, "event-listener").start();
    }

}
