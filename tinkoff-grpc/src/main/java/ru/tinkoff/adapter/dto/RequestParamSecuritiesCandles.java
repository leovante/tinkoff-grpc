package ru.tinkoff.adapter.dto;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestParamSecuritiesCandles extends GeneralRequest {

    private String security;
    private String board;
    private LocalDate from;
    private LocalDate till;
    private Integer interval;
    private Boolean reverse;

}
