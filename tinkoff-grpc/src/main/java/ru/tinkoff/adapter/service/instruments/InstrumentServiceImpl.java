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
@Profile("!sandbox")
public class InstrumentServiceImpl implements InstrumentService {

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
//    @Retryable
    private void init() {
        var shares = tinkoffCommonAPI.getApi().getInstrumentsService().getAllSharesSync();
        shares.forEach(i -> {
            sharesByTicker.put(i.getFigi(), new Instrument(i.getTicker(), i.getCurrency(), i.getName(), i.getLot()));
            tickerToFigi.put(i.getTicker(), i.getFigi());
        });

        var futures = tinkoffCommonAPI.getApi().getInstrumentsService().getAllFuturesSync();
        futures.forEach(i -> {
            futuresByTicker.put(i.getFigi(), new Instrument(i.getTicker(), i.getCurrency(), i.getName(), i.getLot()));
            tickerToFigi.put(i.getTicker(), i.getFigi());
        });

        var bounds = tinkoffCommonAPI.getApi().getInstrumentsService().getAllBondsSync();
        bounds.forEach(i -> {
            boundsByTicker.put(i.getFigi(), new Instrument(i.getTicker(), i.getCurrency(), i.getName(), i.getLot()));
            tickerToFigi.put(i.getTicker(), i.getFigi());
        });

        var etfs = tinkoffCommonAPI.getApi().getInstrumentsService().getAllEtfsSync();
        etfs.forEach(i -> {
            etfsByTicker.put(i.getFigi(), new Instrument(i.getTicker(), i.getCurrency(), i.getName(), i.getLot()));
            tickerToFigi.put(i.getTicker(), i.getFigi());
        });

        var currencies = tinkoffCommonAPI.getApi().getInstrumentsService().getAllCurrenciesSync();
        currencies.forEach(i -> {
            currenciesByTicker.put(i.getFigi(), new Instrument(i.getTicker(), i.getCurrency(), i.getName(), i.getLot()));
            tickerToFigi.put(i.getTicker(), i.getFigi());
        });
    }

}
