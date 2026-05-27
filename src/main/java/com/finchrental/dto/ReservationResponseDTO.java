package com.finchrental.dto;

import com.finchrental.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private ReservationStatus status;
    private BigDecimal totalPrice;
    private List<ReservationItemResponseDTO> items;
}
