package ru.tinkoff.adapter.tinkoff.legacy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.tinkoff.adapter.dto.InstrumentDto;

import java.math.BigDecimal;

public interface ITinkoffOrderAPI {
    @AllArgsConstructor
    @Data
    @Builder
    class OrderResult {
        BigDecimal commission;
        BigDecimal price;
    }

    OrderResult buy(InstrumentDto instrumentDto, BigDecimal price, Integer count);

    OrderResult sell(InstrumentDto instrumentDto, BigDecimal price, Integer count);
}
