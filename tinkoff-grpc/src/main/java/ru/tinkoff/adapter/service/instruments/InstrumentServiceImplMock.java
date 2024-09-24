package ru.tinkoff.adapter.service.instruments;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.tinkoff.adapter.service.tinkoff.ITinkoffCommonAPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to provide details for any trade instrument
 */
@Service
@RequiredArgsConstructor
@Getter
@Slf4j
@Profile("sandbox")
public class InstrumentServiceImplMock implements InstrumentService {

    private final ITinkoffCommonAPI tinkoffCommonAPI;
    private Map<String, String> tickerToFigi = new ConcurrentHashMap<>();
    private Map<String, Instrument> sharesByTicker = new ConcurrentHashMap<>();
    private Map<String, Instrument> futuresByTicker = new ConcurrentHashMap<>();
    private Map<String, Instrument> boundsByTicker = new ConcurrentHashMap<>();
    private Map<String, Instrument> etfsByTicker = new ConcurrentHashMap<>();
    private Map<String, Instrument> currenciesByTicker = new ConcurrentHashMap<>();

    @Override
    public Instrument getShares(String ticker) {
        var instrument = sharesByTicker.get(ticker);
        if (instrument == null) {
            log.warn("Instrument not found: {}", ticker);
        }
        return instrument;
    }

    @Override
    public Instrument getFutures(String ticker) {
        var instrument = futuresByTicker.get(ticker);
        if (instrument == null) {
            log.warn("Instrument not found: {}", ticker);
        }
        return instrument;
    }

    @Override
    public Instrument getBounds(String ticker) {
        var instrument = boundsByTicker.get(ticker);
        if (instrument == null) {
            log.warn("Instrument not found: {}", ticker);
        }
        return instrument;
    }

    @Override
    public Instrument getEtfs(String ticker) {
        var instrument = etfsByTicker.get(ticker);
        if (instrument == null) {
            log.warn("Instrument not found: {}", ticker);
        }
        return instrument;
    }

    @Override
    public Instrument getCurrencies(String ticker) {
        var instrument = currenciesByTicker.get(ticker);
        if (instrument == null) {
            log.warn("Instrument not found: {}", ticker);
        }
        return instrument;
    }

    @Override
    public Instrument getInstrument(String figi) {
        return null;
    }

    @PostConstruct
    private void init() {
        sharesByTicker.put("BBG004S683W7", new Instrument("BBG004S683W7", "RUB", "name", 1));
        tickerToFigi.put("RU0009062285", "BBG004S683W7");
    }

}
