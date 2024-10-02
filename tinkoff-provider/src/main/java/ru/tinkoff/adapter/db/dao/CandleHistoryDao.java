package ru.tinkoff.adapter.db.dao;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.adapter.db.entity.SecuritiesCandlesM1;
import ru.tinkoff.adapter.db.repository.SecuritiesCandlesM1Repository;
import ru.tinkoff.adapter.mapper.CandleMapper;
import ru.tinkoff.piapi.contract.v1.Candle;

/**
 * Service to load and store actual history of candles
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CandleHistoryDao implements CandleMapper {

    private final SecuritiesCandlesM1Repository securitiesCandlesM1Repository;

    @Transactional
    public SecuritiesCandlesM1 upsert(Candle newCandle) {
        if (newCandle.getIntervalValue() != 1) {
            return null;
        }

        var snapshot = mapDtoToEntity(newCandle);

        log.info("Save Candle for {} from {} till {}", newCandle.getFigi(),
                snapshot.getSecuritiesCandlesPk().getBeginAt(), snapshot.getSecuritiesCandlesPk().getEndAt());
        return securitiesCandlesM1Repository.upsert(snapshot,
                snapshot.getSecuritiesCandlesPk().getSecId(),
                snapshot.getSecuritiesCandlesPk().getBeginAt(),
                snapshot.getSecuritiesCandlesPk().getEndAt()
        );
    }

}
