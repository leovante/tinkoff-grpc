package ru.tinkoff.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstrumentDto {
    String ticker;
    String currency;
    String name;
    int lot;
}
