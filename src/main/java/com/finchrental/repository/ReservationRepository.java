package com.finchrental.repository;

import com.finchrental.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(ri) FROM ReservationItem ri WHERE ri.equipment.id = :equipmentId " +
           "AND ri.reservation.status != 'REJECTED' " +
           "AND :startDate <= ri.reservation.endDate " +
           "AND :endDate >= ri.reservation.startDate")
    long countOverlappingReservations(
        @Param("equipmentId") Long equipmentId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
