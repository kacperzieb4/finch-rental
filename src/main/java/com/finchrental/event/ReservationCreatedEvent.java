package com.finchrental.event;

import com.finchrental.entity.Reservation;
import lombok.Getter;

@Getter
public class ReservationCreatedEvent {

    private final Reservation reservation;

    public ReservationCreatedEvent(Reservation reservation) {
        this.reservation = reservation;
    }
}
