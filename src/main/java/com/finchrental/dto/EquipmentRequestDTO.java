package com.finchrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class EquipmentRequestDTO {

    @NotBlank(message = "Nazwa sprzętu nie może być pusta")
    private String name;

    @NotBlank(message = "Kategoria nie może być pusta")
    private String category;

    @Size(max = 500, message = "Opis nie może przekraczać 500 znaków")
    private String description;

    @NotNull(message = "Cena za dzień jest wymagana")
    @Positive(message = "Cena za dzień musi być większa od zera")
    private BigDecimal pricePerDay;

    @NotNull(message = "Status dostępności jest wymagany")
    private Boolean available;
}
