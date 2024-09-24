package ru.tinkoff.adapter.service.candle;

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

public interface CandleMapperUtils {

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

    static BigDecimal toBigDecimal(Quotation value, Integer scale) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        var result = BigDecimal.valueOf(value.getUnits()).add(BigDecimal.valueOf(value.getNano(), 9));
        if (scale != null) {
            return result.setScale(scale, RoundingMode.HALF_EVEN);
        }
        return result;
    }

    static Double toDouble(Quotation value) {
        if (value == null) {
            return 0.0;
        }
        var result = Double.valueOf(value.getNano());
        return result;
    }

    static Double toDouble(Long value) {
        if (value == null) {
            return 0.0;
        }
        var result = Double.valueOf(value);
        return result;
    }

    static Double toDouble(Integer value) {
        if (value == null) {
            return 0.0;
        }
        var result = Double.valueOf(value);
        return result;
    }

    static OffsetDateTime toOffsetDateTime(long seconds) {
        return Instant.ofEpochSecond(seconds).atOffset(ZoneOffset.UTC);
    }

    static LocalDateTime toLocalDateTime(long seconds) {
        return Instant.ofEpochSecond(seconds).atOffset(ZoneOffset.ofHours(3)).toLocalDateTime();
    }

}
