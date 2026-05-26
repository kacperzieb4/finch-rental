package com.finchrental.listener;

import com.finchrental.event.ReservationCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ReservationEventListener.class);

    @EventListener
    public void handleReservationCreatedEvent(ReservationCreatedEvent event) {
        logger.info("New reservation created: ID={}, Customer={}, Equipment={}, Range={} to {}",
                event.getReservation().getId(),
                event.getReservation().getCustomerName(),
                event.getReservation().getEquipment().getName(),
                event.getReservation().getStartDate(),
                event.getReservation().getEndDate()
        );
    }
}
