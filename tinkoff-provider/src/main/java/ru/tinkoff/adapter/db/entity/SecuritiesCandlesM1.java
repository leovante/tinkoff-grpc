package ru.tinkoff.adapter.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SecuritiesCandlesM1 {

    @EmbeddedId
    private SecuritiesCandlesPk securitiesCandlesPk;

    @Column(name = "open", nullable = false)
    private Double open;

    @Column(name = "close", nullable = false)
    private Double close;

    @Column(name = "high", nullable = false)
    private Double high;

    @Column(name = "low", nullable = false)
    private Double low;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "volume", nullable = false)
    private Double volume;

}
