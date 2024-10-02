package ru.tinkoff.adapter.dto;

import java.time.Duration;
import java.time.LocalDate;

import static java.util.concurrent.TimeUnit.DAYS;

public class GeneralRequest {

    public Duration getDuration() {
        return Duration.ofDays(DAYS.toChronoUnit().between(getFrom(), getTill()));
    }


    public LocalDate getFrom() {
        return null;
    }

    public LocalDate getTill() {
        return null;
    }

    public String getBoard() {
        return null;
    }

    public String getSecurity() {
        return null;
    }

    public void setSecurity() {
    }

    public void setFrom(LocalDate from) {
    }

    public void setTill(LocalDate till) {
    }

    public void setBoard(String board) {
    }

}
