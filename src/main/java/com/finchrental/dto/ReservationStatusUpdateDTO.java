package com.finchrental.dto;

import com.finchrental.entity.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusUpdateDTO {

    @NotNull(message = "Status rezerwacji jest wymagany")
    private ReservationStatus status;
}
