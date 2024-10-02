package ru.tinkoff.adapter.tinkoff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import ru.tinkoff.adapter.dto.InstrumentDto;
import ru.tinkoff.adapter.instruments.InstrumentService;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.SubscriptionStatus;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PublisherSharesAdapter {

    private final InvestApi investApi;
    private final ReplayProcessor<Candle> processor;
    private final FluxSink<Candle> sink;
    private final InstrumentService instrumentService;

    public PublisherSharesAdapter(InvestApi investApi,
                                  InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
        this.investApi = investApi;
        this.processor = ReplayProcessor.createTimeout(Duration.ofSeconds(1L));
        this.sink = processor.sink();
    }

    public ReplayProcessor<Candle> start(){
        new Thread(this::startToListen, "event-listener-PublisherSharesAdapter").start();
        return processor;
    }

    private void startToListen() {
        var figis = getFigis(instrumentService.getTickers());
        if (CollectionUtils.isEmpty(figis)) {
            log.warn("No figies to subscribe");
            return;
        }

        log.info("Start to listen candle events..");
        try {
            investApi.getMarketDataStreamService()
                    .newStream("candles_stream", item -> {
                        log.info("New data in streaming api: {}", item);
                        if (item.hasCandle()) {
                            sink.next(item.getCandle());
//                            purchaseService.observeCandleNoThrow(item.getCandle());
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

    private List<String> getFigis(Map<String, InstrumentDto> sharesByTicker) {
        return sharesByTicker.keySet().stream().toList();
    }

}
