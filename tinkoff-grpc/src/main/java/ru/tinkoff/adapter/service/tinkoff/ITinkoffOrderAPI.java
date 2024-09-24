package ru.tinkoff.adapter.service.tinkoff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.tinkoff.adapter.service.instruments.Instrument;
import ru.tinkoff.adapter.service.instruments.InstrumentServiceImpl;

import java.math.BigDecimal;

public interface ITinkoffOrderAPI {
    @AllArgsConstructor
    @Data
    @Builder
    class OrderResult {
        BigDecimal commission;
        BigDecimal price;
    }

    OrderResult buy(Instrument instrument, BigDecimal price, Integer count);

    OrderResult sell(Instrument instrument, BigDecimal price, Integer count);
}
