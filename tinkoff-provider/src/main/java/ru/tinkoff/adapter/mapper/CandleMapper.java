package ru.tinkoff.adapter.mapper;

import ru.tinkoff.adapter.db.entity.SecuritiesCandlesM1;
import ru.tinkoff.adapter.db.entity.SecuritiesCandlesPk;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public interface CandleMapper extends UtilsMapper {

    default SecuritiesCandlesM1 mapDtoToEntity(Candle candle){
        return SecuritiesCandlesM1.builder()
                .securitiesCandlesPk(SecuritiesCandlesPk.builder()
                        .secId(candle.getFigi())
                        .beginAt(toLocalDateTime(candle.getTime().getSeconds()))
                        .endAt(toLocalDateTime(candle.getTime().getSeconds() + candle.getIntervalValue() * 60L))
                        .build())
                .open(toBigDecimal(candle.getOpen(),4).doubleValue())
                .close(toBigDecimal(candle.getClose(),4).doubleValue())
                .high(toBigDecimal(candle.getHigh(),4).doubleValue())
                .low(toBigDecimal(candle.getLow(),4).doubleValue())
                .volume(toDouble(candle.getVolume()))
                .value(toDouble(candle.getIntervalValue()))
                .build();
    }

}
