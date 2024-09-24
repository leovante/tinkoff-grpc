package ru.tinkoff.adapter.service.instruments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instrument {
    String ticker;
    String currency;
    String name;
    int lot;
}
