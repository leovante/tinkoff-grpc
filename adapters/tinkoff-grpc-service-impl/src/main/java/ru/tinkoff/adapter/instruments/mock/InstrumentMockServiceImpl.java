package ru.tinkoff.adapter.instruments.mock;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.tinkoff.adapter.dto.InstrumentDto;
import ru.tinkoff.adapter.instruments.InstrumentService;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to provide details for any trade instrument
 */
@Service("InstrumentShares")
@RequiredArgsConstructor
@Getter
@Slf4j
@Profile("sandbox")
public class InstrumentMockServiceImpl implements InstrumentService {

    private Map<String, InstrumentDto> tickers = new ConcurrentHashMap<>();
    private Map<String, String> tickerToFigi = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        tickers.put("BBG004S683W7", new InstrumentDto("BBG004S683W7", "RUB", "name", 1));
        tickers.put("BBG004730JJ5", new InstrumentDto("BBG004S683W7", "RUB", "name", 1));
        tickerToFigi.put("RU0009062285", "BBG004S683W7");
        tickerToFigi.put("RU000A0JR4A1", "BBG004730JJ5");
    }

}
