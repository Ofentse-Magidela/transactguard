package com.transactguard.transactguard.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class SendMoneyRequestDTO {

    @NotNull(message = "Sender ID is required.")
    @Min(value = 1, message = "Id Need To Be Equals Or Greater Than One")
    private Long senderID;

    @NotNull(message = "Recipient ID is required.")
    @Min(value = 1, message = "Enter a valid ID")
    private Long receiverID;

    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "1.00", message = "Minimum amount is R1.00")
    private Double amount;
}
