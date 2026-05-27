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
        String equipmentNames = "";
        if (event.getReservation().getItems() != null) {
            equipmentNames = event.getReservation().getItems().stream()
                    .map(item -> item.getEquipment() != null ? item.getEquipment().getName() : "Unknown")
                    .collect(java.util.stream.Collectors.joining(", "));
        }

        logger.info("New reservation created: ID={}, Customer={}, Equipment={}, Range={} to {}",
                event.getReservation().getId(),
                event.getReservation().getCustomerName(),
                equipmentNames,
                event.getReservation().getStartDate(),
                event.getReservation().getEndDate()
        );
    }
}
