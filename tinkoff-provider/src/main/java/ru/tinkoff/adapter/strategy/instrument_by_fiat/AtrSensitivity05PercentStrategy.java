package ru.tinkoff.adapter.strategy.instrument_by_fiat;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.adapter.instruments.InstrumentService;
import ru.tinkoff.adapter.service.moexiss.MoexIssSyncService;
import ru.tinkoff.adapter.service.order.OrderService;
import ru.tinkoff.piapi.contract.v1.Candle;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AtrSensitivity05PercentStrategy extends AInstrumentByFiatStrategy {

    private final MoexIssSyncService moexIssSyncService;
    private final OrderService orderService;
    private final InstrumentService instrumentService;

    private static Map<String, Integer> FIGIES;

    public Map<String, Integer> getFigies() {
        return FIGIES;
    }

    @Override
    public void exec(Candle candle) {
        process(candle);
    }

    private void process(Candle candle) {
        var order = orderService.findActiveByFigiAndStrategy(candle.getFigi(), this);

        // Нет активной позиции по стратегии -> возможно можем купить, иначе продать
        if (order == null) {
            buy(candle);
        } else {
            sell(candle);
        }
    }

    private void buy(Candle candle) {
        /*// Проверяем, включена ли покупка по стратегии (выключаем в случае завершения торговли по стратегии)
        if (strategy.isOnlySell()) {
            return;
        }

        // Рассчитываем, нужно ли покупать
        var isShouldBuy = calculator.isShouldBuy(strategy, candle);
        if (!isShouldBuy) {
            return;
        }

        // Проверяем задержку в торговле после продажи по stop loss
        if (strategy.getType() == AStrategy.Type.instrumentByFiat && strategy.getDelayBySL() != null) {
            var finishedOrders = orderService.findClosedByFigiAndStrategy(candle.getFigi(), strategy);
            if (finishedOrders.isEmpty()) {
                var lastOrder = finishedOrders.getLast();
                if (ChronoUnit.SECONDS.between(lastOrder.getSellDateTime(), candle.getDateTime()) < strategy.getDelayBySL().getSeconds()) {
                    return;
                }
            }
        }

        // Покупаем
        var order = orderService.buy(candle, strategy);
        var msg = String.format("Buy %s (%s), %s x%s, %s, %s. Wanted %s", order.getFigi(),
                order.getFigiTitle(), order.getPurchasePrice(), order.getLots(), order.getPurchaseDateTime(),
                order.getStrategy(), candle.getClosingPrice());
        log.info(msg);
        logIfAmountChanged(candle, order.getPurchasePrice());
        notificationService.sendMessage(msg);*/
    }

    private void sell(Candle candle) {
       /* // Позиция есть, но пришла свечка с датой до покупки текущего ордера, так не должно быть
        if (order.getPurchaseDateTime().isAfter(candle.getDateTime())) {
            log.error("Was founded order before current candle date time: {}, {}", order, candle);
            return;
        }

        // Рассчитываем, нужно ли продавать
        var isShouldSell = calculator.isShouldSell(strategy, candle, order.getPurchasePrice());
        if (!isShouldSell) {
            return;
        }

        // Продаем
        order = orderService.sell(candle, strategy);
        var msg = String.format("Sell %s (%s), %s x%s (%s), %s, %s. Wanted: %s", candle.getFigi(),
                order.getFigiTitle(), order.getSellPrice(), order.getLots(), order.getSellProfit(),
                order.getSellDateTime(), order.getStrategy(), candle.getClosingPrice());
        log.info(msg);
        logIfAmountChanged(candle, order.getSellPrice());
        notificationService.sendMessage(msg);*/
    }

    private void logIfAmountChanged(Candle candle, BigDecimal amountFact) {
        /*try {
            var distance = candle.getClosingPrice().subtract(amountFact).abs();
            var percent = BigDecimal.valueOf(0.001);
            if (distance.divide(amountFact, 4, RoundingMode.HALF_EVEN).compareTo(percent) > 0) {
                log.warn("Price for {} significantly changed: {} -> {}", candle.getFigi(), candle.getClosingPrice(), amountFact);
            }
        } catch (Exception e) {
            log.error("An error during logIfAmountChanged", e);
        }*/
    }

    @Override
    public AInstrumentByFiatStrategy.BuyCriteria getBuyCriteria() {
        return AInstrumentByFiatStrategy.BuyCriteria.builder().lessThenPercentile(10).build();
    }

    @Override
    public AInstrumentByFiatStrategy.SellCriteria getSellCriteria() {
        return AInstrumentByFiatStrategy.SellCriteria.builder().takeProfitPercent(1f).stopLossPercent(3f).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PostConstruct
    private void init(){
        var isinList = moexIssSyncService.fetchIsinList(1);
        FIGIES = new HashMap<>();
        isinList.forEach(i -> FIGIES.put(instrumentService.getTickerToFigi().get(i), 1));
    }

}
// добавить кеш на fetchSpecs
// добавить ретрай
