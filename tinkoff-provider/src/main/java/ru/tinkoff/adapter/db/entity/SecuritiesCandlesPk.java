package ru.tinkoff.adapter.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class SecuritiesCandlesPk {

    @Column(name = "sec_id", nullable = false)
    private String secId;

    @Column(name = "begin_at", nullable = false)
    private LocalDateTime beginAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

}
