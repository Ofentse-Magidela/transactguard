package com.transactguard.transactguard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMoneyRequestDTO {
    private Long senderID;
    private Long receiverID;
    private Double amount;
}
