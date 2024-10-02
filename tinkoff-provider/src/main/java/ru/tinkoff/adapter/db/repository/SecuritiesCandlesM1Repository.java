package ru.tinkoff.adapter.db.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tinkoff.adapter.db.entity.SecuritiesCandlesM1;

import java.time.LocalDateTime;

public interface SecuritiesCandlesM1Repository extends JpaRepository<SecuritiesCandlesM1, Long> {

    @Query(value = """
            insert into securities_candles_m1 (sec_id, begin_at, end_at, open, close, high, low, value, volume)
            values (:#{#secId}, :#{#beginAt}, :#{#endAt}, :#{#entity.open}, :#{#entity.close},
                    :#{#entity.high}, :#{#entity.low}, :#{#entity.value}, :#{#entity.volume})
            on conflict(sec_id, begin_at, end_at)
                do update set sec_id   = coalesce(excluded.sec_id, securities_candles_m1.sec_id),
                              begin_at = coalesce(excluded.begin_at, securities_candles_m1.begin_at),
                              end_at   = coalesce(excluded.end_at, securities_candles_m1.end_at),
                              open     = coalesce(excluded.open, securities_candles_m1.open),
                              close    = coalesce(excluded.close, securities_candles_m1.close),
                              high     = coalesce(excluded.high, securities_candles_m1.high),
                              low      = coalesce(excluded.low, securities_candles_m1.low),
                              value    = coalesce(excluded.value, securities_candles_m1.value),
                              volume   = coalesce(excluded.volume, securities_candles_m1.volume)
            returning *
            """, nativeQuery = true)
    SecuritiesCandlesM1 upsert(SecuritiesCandlesM1 entity, String secId, LocalDateTime beginAt, LocalDateTime endAt);

}
