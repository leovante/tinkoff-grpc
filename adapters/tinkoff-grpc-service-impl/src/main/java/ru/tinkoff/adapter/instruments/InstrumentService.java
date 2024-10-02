package ru.tinkoff.adapter.instruments;

import ru.tinkoff.adapter.dto.InstrumentDto;

import java.util.Map;

public interface InstrumentService {

    Map<String, InstrumentDto> getTickers();

    Map<String, String> getTickerToFigi();

}
