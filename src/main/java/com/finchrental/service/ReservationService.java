package com.finchrental.service;

import com.finchrental.entity.Equipment;
import com.finchrental.entity.Reservation;
import com.finchrental.entity.ReservationStatus;
import com.finchrental.event.ReservationCreatedEvent;
import com.finchrental.exception.ResourceNotFoundException;
import com.finchrental.repository.EquipmentRepository;
import com.finchrental.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Reservation createReservation(Reservation reservation, Long equipmentId) {
        // 1. Walidacja poprawności dat (endDate po startDate lub równa)
        if (reservation.getEndDate().isBefore(reservation.getStartDate())) {
            throw new IllegalArgumentException("Data zakończenia rezerwacji nie może być przed datą rozpoczęcia");
        }

        // 2. Pobranie powiązanego sprzętu
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprzęt o podanym ID nie istnieje: " + equipmentId));

        // 3. Sprawdzenie konfliktów datowych (czy sprzęt jest dostępny)
        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                equipmentId,
                reservation.getStartDate(),
                reservation.getEndDate()
        );
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Sprzęt jest już zarezerwowany w tym przedziale czasowym");
        }

        // 4. Przypisanie sprzętu i domyślnego statusu
        reservation.setEquipment(equipment);
        reservation.setStatus(ReservationStatus.PENDING);

        // 5. Zapis
        Reservation savedReservation = reservationRepository.save(reservation);

        // 6. Publikacja zdarzenia
        eventPublisher.publishEvent(new ReservationCreatedEvent(savedReservation));

        return savedReservation;
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rezerwacja o podanym ID nie istnieje: " + id));
        reservationRepository.delete(reservation);
    }
}
