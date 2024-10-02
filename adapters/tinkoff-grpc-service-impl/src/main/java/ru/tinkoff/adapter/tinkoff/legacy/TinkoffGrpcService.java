package ru.tinkoff.adapter.tinkoff.legacy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.adapter.dto.InstrumentDto;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@Slf4j
@ConditionalOnProperty(name = "tinkoff.emulator", havingValue = "false")
@RequiredArgsConstructor
public class TinkoffGrpcService extends ATinkoffAPI {

    public OrderResult buy(InstrumentDto instrumentDto, BigDecimal price, Integer count) {
        long quantity = count / instrumentDto.getLot();
        var quotation = Quotation.newBuilder()
                .setUnits(price.longValue())
                .setNano(price.remainder(BigDecimal.ONE).movePointRight(9).intValue())
                .build();
        var uuid = UUID.randomUUID().toString();
        log.info("Send postOrderSync with: figi {}, quantity {}, quotation {}, direction {}, acc {}, type {}, id {}",
                instrumentDto.getTicker(), quantity, quotation, OrderDirection.ORDER_DIRECTION_BUY, getAccountId(), OrderType.ORDER_TYPE_MARKET, uuid);

        if (getIsSandboxMode()) {
            var result = getApi().getSandboxService().postOrderSync(instrumentDto.getTicker(), quantity, quotation,
                    OrderDirection.ORDER_DIRECTION_BUY, getAccountId(), OrderType.ORDER_TYPE_MARKET, uuid);
            return OrderResult.builder()
                    .commission(toBigDecimal(result.getInitialCommission(), 8))
                    .price(toBigDecimal(result.getExecutedOrderPrice(), 4, price))
                    .build();
        } else {
            var result = getApi().getOrdersService().postOrderSync(instrumentDto.getTicker(), quantity, quotation,
                    OrderDirection.ORDER_DIRECTION_BUY, getAccountId(), OrderType.ORDER_TYPE_MARKET, uuid);
            return OrderResult.builder()
                    .commission(toBigDecimal(result.getInitialCommission(), 8))
                    .price(toBigDecimal(result.getExecutedOrderPrice(), 4, price))
                    .build();
        }
    }

    public OrderResult sell(InstrumentDto instrumentDto, BigDecimal price, Integer count) {
        long quantity = count / instrumentDto.getLot();
        var quotation = Quotation.newBuilder()
                .setUnits(price.longValue())
                .setUnits(price.remainder(BigDecimal.ONE).movePointRight(9).intValue())
                .build();
        var uuid = UUID.randomUUID().toString();
        log.info("Send postOrderSync with: figi {}, quantity {}, quotation {}, direction {}, acc {}, type {}, id {}",
                instrumentDto.getTicker(), quantity, quotation, OrderDirection.ORDER_DIRECTION_SELL, getAccountId(), OrderType.ORDER_TYPE_MARKET, uuid);

        if (getIsSandboxMode()) {
            var result = getApi().getSandboxService().postOrderSync(instrumentDto.getTicker(), quantity, quotation,
                    OrderDirection.ORDER_DIRECTION_SELL, getAccountId(), OrderType.ORDER_TYPE_MARKET, uuid);
            return OrderResult.builder()
                    .commission(toBigDecimal(result.getInitialCommission(), 8))
                    .price(toBigDecimal(result.getExecutedOrderPrice(), 4, price))
                    .build();
        } else {
            var result = getApi().getOrdersService().postOrderSync(instrumentDto.getTicker(), quantity, quotation,
                    OrderDirection.ORDER_DIRECTION_SELL, getAccountId(), OrderType.ORDER_TYPE_MARKET, uuid);
            return OrderResult.builder()
                    .commission(toBigDecimal(result.getInitialCommission(), 8))
                    .price(toBigDecimal(result.getExecutedOrderPrice(), 4, price))
                    .build();
        }
    }

    public static BigDecimal toBigDecimal(MoneyValue value, Integer scale) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        var result = BigDecimal.valueOf(value.getUnits()).add(BigDecimal.valueOf(value.getNano(), 9));
        if (scale != null) {
            return result.setScale(scale, RoundingMode.HALF_EVEN);
        }
        return result;
    }

    /**
     * @param value
     * @param scale
     * @param defaultIfZero - default value required because of executedPrice in sandbox = 0
     * @return
     */
    public static BigDecimal toBigDecimal(MoneyValue value, Integer scale, BigDecimal defaultIfZero) {
        var amount = toBigDecimal(value, scale);
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return defaultIfZero;
        }
        return amount;
    }
}
