package ru.tinkoff.adapter.service.order;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.tinkoff.adapter.db.entity.OrderDomainEntity;
import ru.tinkoff.adapter.db.repository.OrderRepository;
import ru.tinkoff.adapter.instruments.InstrumentService;
import ru.tinkoff.adapter.tinkoff.legacy.ITinkoffOrderAPI;
import ru.tinkoff.adapter.strategy.AStrategy;
//import ru.tinkoff.piapi.contract.v1.Candle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InstrumentService instrumentService;
//    private final ITinkoffOrderAPI tinkoffOrderAPI;

    private volatile List<OrderDomainEntity> orders;

    public OrderDomainEntity findActiveByFigiAndStrategy(String figi, AStrategy strategy) {
        return orders.stream()
                .filter(o -> figi == null || o.getFigi().equals(figi))
                .filter(o -> o.getSellDateTime() == null)
                .filter(o -> o.getStrategy().equals(strategy.getName()))
                .findFirst().orElse(null);
    }

    public List<OrderDomainEntity> findClosedByFigiAndStrategy(String figi, AStrategy strategy) {
        return orders.stream()
                .filter(o -> o.getFigi().equals(figi))
                .filter(o -> o.getSellDateTime() != null)
                .filter(o -> o.getStrategy().equals(strategy.getName()))
                .toList();
    }

    public OrderDomainEntity findLastByFigiAndStrategy(String figi, AStrategy strategy) {
        return orders.stream()
                .filter(o -> figi == null || o.getFigi().equals(figi))
                .filter(o -> o.getSellDateTime() != null)
                .filter(o -> o.getStrategy().equals(strategy.getName()))
                .reduce((first, second) -> second)
                .orElse(null);
    }

//    @Transactional(Transactional.TxType.REQUIRES_NEW)
//    public synchronized OrderDomainEntity buy(Candle candle, AStrategy strategy) {
        /*// request to buy position
        var instrument = instrumentService.getInstrument(candle.getFigi());
        var lots = strategy.getCount(candle.getFigi());
        var response = tinkoffOrderAPI.buy(instrument, candle.getClosingPrice(), lots);

        // update prices from response
        pricesService.updatePrice(instrument.getFigi(), response.getPrice());

        // save order to DB
        var currentPrices = pricesService.getCurrentPrices(strategy);
        var orderDetails = OrderDetails.builder()
                .currentPrices(currentPrices)
                .build();
        var order = OrderDomainEntity.builder()
                .currency(instrument.getCurrency())
                .figi(instrument.getFigi())
                .figiTitle(instrument.getName())
                .purchasePrice(response.getPrice())
                .purchaseCommission(response.getCommission())
                .strategy(strategy.getName())
                .purchaseDateTime(candle.getDateTime())
                .lots(lots)
                .purchaseCommission(response.getCommission())
                .details(orderDetails)
                .build();
        order = orderRepository.save(order);

        // update order list in memory
        orders.add(order);

        return order;*/
//        return null;
//    }

//    @Transactional(Transactional.TxType.REQUIRES_NEW)
//    public synchronized OrderDomainEntity sell(Candle candle, AStrategy strategy) {
        /*// find position to sell
        var order = findActiveByFigiAndStrategy(candle.getFigi(), strategy);
        if (order == null) {
            var s = String.format("No active order to sell by %s, %s", candle.getFigi(), strategy.getName());
            throw new RuntimeException(s);
        }

        // request to sell position
        var instrument = instrumentService.getInstrument(candle.getFigi());
        var response = tinkoffOrderAPI.sell(instrument, candle.getClosingPrice(), order.getLots());

        // update prices from response
        if (response.getPrice() != null) {
            pricesService.updatePrice(instrument.getFigi(), response.getPrice());
        }

        // update order in DB
        order.setSellDateTime(OffsetDateTime.now());
        order.setSellCommission(response.getCommission());
        order.setSellPrice(response.getPrice() == null ? candle.getClosingPrice() : response.getPrice());
        order.setSellProfit(response.getPrice().subtract(order.getPurchasePrice()));
        if (order.getDetails() != null && response.getPrice() != null) {
            order.getDetails().setSellPrices(pricesService.getCurrentPrices(strategy));
        }
        order = orderRepository.save(order);

        // update order list in memory
        var orderId = order.getId();
        var orderInList = orders.stream()
                .filter(o -> o.getId().equals(orderId)).findFirst()
                .orElseThrow();
        orders.remove(orderInList);
        orders.add(order);

        return order;*/
//        return null;
//    }

    @PostConstruct
    public void loadOrdersFromDB() {
        orders = new CopyOnWriteArrayList<>();
        orders.addAll(orderRepository.findAll(Sort.by("id")));
    }
}
