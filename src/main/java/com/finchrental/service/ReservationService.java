package com.finchrental.service;

import com.finchrental.entity.Equipment;
import com.finchrental.entity.Reservation;
import com.finchrental.entity.ReservationItem;
import com.finchrental.entity.ReservationStatus;
import com.finchrental.event.ReservationCreatedEvent;
import com.finchrental.exception.ResourceNotFoundException;
import com.finchrental.repository.EquipmentRepository;
import com.finchrental.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EquipmentRepository equipmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              EquipmentRepository equipmentRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.reservationRepository = reservationRepository;
        this.equipmentRepository = equipmentRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public Reservation createReservation(Reservation reservation, List<Long> equipmentIds) {
        if (equipmentIds == null || equipmentIds.isEmpty()) {
            throw new IllegalArgumentException("Koszyk nie może być pusty");
        }

        if (reservation.getEndDate().isBefore(reservation.getStartDate())) {
            throw new IllegalArgumentException("Data zakończenia rezerwacji nie może być przed datą rozpoczęcia");
        }

        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate maxDate = today.plusMonths(3);

        if (reservation.getStartDate().isBefore(today)) {
            throw new IllegalArgumentException("Nie można rezerwować sprzętu wstecz (przed dzisiejszym dniem)");
        }

        if (reservation.getEndDate().isAfter(maxDate)) {
            throw new IllegalArgumentException("Nie można rezerwować sprzętu na okres wykraczający poza 3 miesiące od dzisiaj");
        }

        long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()) + 1;

        List<ReservationItem> reservationItems = new ArrayList<>();
        List<Long> localAllocated = new ArrayList<>();
        BigDecimal totalSum = BigDecimal.ZERO;

        for (Long eqId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(eqId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sprzęt o podanym ID nie istnieje: " + eqId));

            if (!Boolean.TRUE.equals(equipment.getAvailable())) {
                throw new IllegalArgumentException("Wybrany sprzęt jest wyłączony z wypożyczania: " + equipment.getName());
            }

            long dbOccupied = reservationRepository.countOverlappingReservations(
                    eqId,
                    reservation.getStartDate(),
                    reservation.getEndDate()
            );

            long localOccupied = localAllocated.stream().filter(id -> id.equals(eqId)).count();

            int maxQuantity = equipment.getQuantity() != null ? equipment.getQuantity() : 1;

            if (dbOccupied + localOccupied >= maxQuantity) {
                throw new IllegalArgumentException("Brak dostępnych sztuk sprzętu: " + equipment.getName() + " w wybranym terminie");
            }

            localAllocated.add(eqId);

            ReservationItem reservationItem = ReservationItem.builder()
                    .reservation(reservation)
                    .equipment(equipment)
                    .pricePerDay(equipment.getPricePerDay() != null ? equipment.getPricePerDay() : BigDecimal.ZERO)
                    .build();

            reservationItems.add(reservationItem);

            BigDecimal itemCost = reservationItem.getPricePerDay().multiply(BigDecimal.valueOf(days));
            totalSum = totalSum.add(itemCost);
        }

        reservation.setItems(reservationItems);
        reservation.setTotalPrice(totalSum);
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation savedReservation = reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationCreatedEvent(savedReservation));

        return savedReservation;
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rezerwacja o podanym ID nie istnieje: " + id));
        reservationRepository.delete(reservation);
    }

    @Transactional
    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rezerwacja o podanym ID nie istnieje: " + id));
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }
}
