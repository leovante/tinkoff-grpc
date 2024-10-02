package ru.tinkoff.adapter.mapper;

import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public interface UtilsMapper {

    default BigDecimal toBigDecimal(Quotation value, Integer scale) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        var result = BigDecimal.valueOf(value.getUnits()).add(BigDecimal.valueOf(value.getNano(), 9));
        if (scale != null) {
            return result.setScale(scale, RoundingMode.HALF_EVEN);
        }
        return result;
    }

    default Double toDouble(Quotation value) {
        if (value == null) {
            return 0.0;
        }
        var result = Double.valueOf(value.getNano());
        return result;
    }

    default Double toDouble(Long value) {
        if (value == null) {
            return 0.0;
        }
        var result = Double.valueOf(value);
        return result;
    }

    default Double toDouble(Integer value) {
        if (value == null) {
            return 0.0;
        }
        var result = Double.valueOf(value);
        return result;
    }

    default OffsetDateTime toOffsetDateTime(long seconds) {
        return Instant.ofEpochSecond(seconds).atOffset(ZoneOffset.UTC);
    }

    default LocalDateTime toLocalDateTime(long seconds) {
        return Instant.ofEpochSecond(seconds).atOffset(ZoneOffset.ofHours(3)).toLocalDateTime();
    }

}
