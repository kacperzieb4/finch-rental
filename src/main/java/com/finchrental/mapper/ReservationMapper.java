package com.finchrental.mapper;

import com.finchrental.dto.ReservationItemResponseDTO;
import com.finchrental.dto.ReservationResponseDTO;
import com.finchrental.dto.ReservationRequestDTO;
import com.finchrental.entity.Reservation;
import com.finchrental.entity.ReservationItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    public ReservationResponseDTO toResponseDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        List<ReservationItemResponseDTO> itemDTOs = Collections.emptyList();
        if (reservation.getItems() != null) {
            itemDTOs = reservation.getItems().stream()
                    .map(this::toItemResponseDTO)
                    .collect(Collectors.toList());
        }

        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .customerName(reservation.getCustomerName())
                .customerEmail(reservation.getCustomerEmail())
                .customerPhone(reservation.getCustomerPhone())
                .status(reservation.getStatus())
                .totalPrice(reservation.getTotalPrice())
                .items(itemDTOs)
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

    private ReservationItemResponseDTO toItemResponseDTO(ReservationItem item) {
        if (item == null) {
            return null;
        }
        return ReservationItemResponseDTO.builder()
                .id(item.getId())
                .equipmentId(item.getEquipment().getId())
                .equipmentName(item.getEquipment().getName())
                .pricePerDay(item.getPricePerDay())
                .build();
    }
}
