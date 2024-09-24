package ru.tinkoff.adapter.service.instruments;

import java.util.Map;

public interface InstrumentService {

    Instrument getShares(String ticker);

    Map<String, Instrument> getSharesByTicker();

    Map<String, String> getTickerToFigi();

    Instrument getFutures(String ticker);

    Instrument getBounds(String ticker);

    Instrument getEtfs(String ticker);

    Instrument getCurrencies(String ticker);

    Instrument getInstrument(String figi);

}
