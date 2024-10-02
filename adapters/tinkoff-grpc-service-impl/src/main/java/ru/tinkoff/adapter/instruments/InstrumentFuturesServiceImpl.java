package ru.tinkoff.adapter.instruments;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.tinkoff.adapter.dto.InstrumentDto;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to provide details for any trade instrument
 */
@Service("InstrumentFutures")
@RequiredArgsConstructor
@Getter
@Slf4j
@Profile("!sandbox")
@Lazy
public class InstrumentFuturesServiceImpl implements InstrumentService {

    private final InvestApi investApi;
    private Map<String, InstrumentDto> tickers = new ConcurrentHashMap<>();
    private Map<String, String> tickerToFigi = new ConcurrentHashMap<>();

    @PostConstruct
//    @Retryable
    private void init() {
        var futures = investApi.getInstrumentsService().getAllFuturesSync();
        futures.forEach(i -> {
            tickers.put(i.getFigi(), new InstrumentDto(i.getTicker(), i.getCurrency(), i.getName(), i.getLot()));
            tickerToFigi.put(i.getTicker(), i.getFigi());
        });
    }

}
