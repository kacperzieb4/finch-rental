package com.finchrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {

    @NotNull(message = "Identyfikator sprzętu jest wymagany")
    private Long equipmentId;

    @NotNull(message = "Data rozpoczęcia rezerwacji jest wymagana")
    @FutureOrPresent(message = "Data rozpoczęcia rezerwacji nie może być w przeszłości")
    private LocalDate startDate;

    @NotNull(message = "Data zakończenia rezerwacji jest wymagana")
    private LocalDate endDate;

    @NotBlank(message = "Nazwa klienta nie może być pusta")
    private String customerName;

    @NotBlank(message = "E-mail klienta nie może być pusty")
    @Email(message = "Niepoprawny format adresu e-mail")
    private String customerEmail;

    private String customerPhone;
}
