package ru.tinkoff.adapter.service.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.adapter.service.candle.CandleHistoryService;
import ru.tinkoff.adapter.service.notification.NotificationService;
import ru.tinkoff.adapter.strategy.AStrategy;
import ru.tinkoff.adapter.strategy.StrategySelector;
import ru.tinkoff.piapi.contract.v1.Candle;


/**
 * Сервис обрабатывает поток свечей
 * Принимает решение с помощью калькулятора о покупке/продаже инструмента в рамках включенных стратегий
 * Отправляет ордеры (покупка, продажа) на исполнение
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseService {

    private final StrategySelector strategySelector;
    private final NotificationService notificationService;
    private final CandleHistoryService candleHistoryService;

    public synchronized void observeCandleNoThrow(Candle candle) {
        try {
            log.debug("Observe candle event: {}", candle);
            var entity = candleHistoryService.upsert(candle);
            var strategies = strategySelector.suitableByFigi(candle.getFigi(), null);
            strategies.parallelStream().forEach(strategy -> observeCandle(candle, strategy));
        } catch (Exception e) {
            var msg = String.format("An error during observe new candle %s, %s", candle, e.getMessage());
            log.error(msg, e);
            notificationService.sendMessage(msg);
        }
    }

    private void observeCandle(Candle candle, AStrategy strategy) {
        strategy.exec(candle);
    }

}
