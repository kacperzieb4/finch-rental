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
public class EquipmentResponseDTO {

    private Long id;
    private String name;
    private String category;
    private String description;
    private BigDecimal pricePerDay;
    private Boolean available;
}
