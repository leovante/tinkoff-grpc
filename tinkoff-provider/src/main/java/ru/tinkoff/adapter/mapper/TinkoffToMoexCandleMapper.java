package ru.tinkoff.adapter.mapper;

import ru.tinkoff.adapter.client.moexIss.payload.Row;
import ru.tinkoff.piapi.contract.v1.Candle;

public interface TinkoffToMoexCandleMapper extends UtilsMapper{

    default Row mapCandleToRow(Candle candle){
        return new Row()
                .open(toBigDecimal(candle.getOpen(),4).doubleValue())
                .close(toBigDecimal(candle.getClose(),4).doubleValue())
                .high(toBigDecimal(candle.getHigh(),4).doubleValue())
                .low(toBigDecimal(candle.getLow(),4).doubleValue())
                .volume(toDouble(candle.getVolume()))
                .value(toDouble(candle.getIntervalValue()))
                ;
    }

}
