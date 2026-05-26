package com.finchrental.mapper;

import com.finchrental.dto.ReservationRequestDTO;
import com.finchrental.dto.ReservationResponseDTO;
import com.finchrental.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponseDTO toResponseDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .customerName(reservation.getCustomerName())
                .customerEmail(reservation.getCustomerEmail())
                .customerPhone(reservation.getCustomerPhone())
                .status(reservation.getStatus())
                .equipmentId(reservation.getEquipment() != null ? reservation.getEquipment().getId() : null)
                .equipmentName(reservation.getEquipment() != null ? reservation.getEquipment().getName() : null)
                .build();
    }

    public Reservation toEntity(ReservationRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Reservation.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .customerName(dto.getCustomerName())
                .customerEmail(dto.getCustomerEmail())
                .customerPhone(dto.getCustomerPhone())
                .build();
    }
}
