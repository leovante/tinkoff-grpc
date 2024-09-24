package ru.tinkoff.adapter.service.candle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.adapter.client.moexIss.payload.Row;
import ru.tinkoff.adapter.dto.RequestParamSecuritiesCandles;
import ru.tinkoff.adapter.service.moexiss.MoexIssService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleProducerService {

    private final MoexIssService moexIssService;

    public List<Row> fetch(RequestParamSecuritiesCandles requestParamSecuritiesCandles) {
        return moexIssService.fetchCandles(requestParamSecuritiesCandles);
    }

}
