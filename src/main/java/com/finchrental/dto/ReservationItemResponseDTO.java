package com.finchrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationItemResponseDTO {
    private Long id;
    private Long equipmentId;
    private String equipmentName;
    private BigDecimal pricePerDay;
}
