package com.transactguard.transactguard.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class SendMoneyRequestDTO {

    @NotNull
    @Min(value = 1, message = "Id Need To Be Equals Or Greater Than One")
    private Long senderID;

    @NotNull
    @Min(value = 1, message = "Id Need To Be  Or Greater Than One")
    private Long receiverID;

    @NotNull
    @DecimalMin(value = "1.00", message = "Money To Be Send Needs To Be 1.00 ZAR Or Greater Than 1.00 ZAR")
    private Double amount;
}
