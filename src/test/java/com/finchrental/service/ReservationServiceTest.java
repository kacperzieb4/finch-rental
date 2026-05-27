package com.finchrental.service;

import com.finchrental.entity.Equipment;
import com.finchrental.entity.Reservation;
import com.finchrental.entity.ReservationStatus;
import com.finchrental.event.ReservationCreatedEvent;
import com.finchrental.exception.ResourceNotFoundException;
import com.finchrental.repository.EquipmentRepository;
import com.finchrental.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ReservationService reservationService;

    private Equipment equipment;
    private Reservation newReservation;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder()
                .id(1L)
                .name("Sony A7IV")
                .category("Camera")
                .description("Full-frame mirrorless camera")
                .pricePerDay(BigDecimal.valueOf(200.00))
                .available(true)
                .quantity(2)
                .build();

        newReservation = Reservation.builder()
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(5))
                .customerName("Jan Kowalski")
                .customerEmail("jan.kowalski@example.com")
                .customerPhone("123456789")
                .build();
    }

    @Test
    void createReservation_Success() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(reservationRepository.countOverlappingReservations(1L, newReservation.getStartDate(), newReservation.getEndDate()))
                .thenReturn(0L);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation r = invocation.getArgument(0);
            r.setId(10L);
            return r;
        });

        Reservation result = reservationService.createReservation(newReservation, List.of(1L));

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(ReservationStatus.PENDING, result.getStatus());
        assertEquals(1, result.getItems().size());
        assertEquals(equipment, result.getItems().get(0).getEquipment());
        assertEquals(BigDecimal.valueOf(800.00), result.getTotalPrice());

        verify(reservationRepository, times(1)).save(newReservation);
        
        ArgumentCaptor<ReservationCreatedEvent> eventCaptor = ArgumentCaptor.forClass(ReservationCreatedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        assertEquals(result, eventCaptor.getValue().getReservation());
    }

    @Test
    void createReservation_ConflictDates_ThrowsException() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(reservationRepository.countOverlappingReservations(1L, newReservation.getStartDate(), newReservation.getEndDate()))
                .thenReturn(2L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation(newReservation, List.of(1L))
        );

        assertEquals("Brak dostępnych sztuk sprzętu: Sony A7IV w wybranym terminie", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void createReservation_EndDateBeforeStartDate_ThrowsException() {
        newReservation.setStartDate(LocalDate.now().plusDays(5));
        newReservation.setEndDate(LocalDate.now().plusDays(2));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation(newReservation, List.of(1L))
        );

        assertEquals("Data zakończenia rezerwacji nie może być przed datą rozpoczęcia", exception.getMessage());
        verify(equipmentRepository, never()).findById(any());
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void createReservation_EquipmentNotFound_ThrowsException() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                reservationService.createReservation(newReservation, List.of(1L))
        );

        assertEquals("Sprzęt o podanym ID nie istnieje: 1", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(eventPublisher, never()).publishEvent(any());
    }
}
