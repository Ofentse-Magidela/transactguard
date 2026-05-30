package com.transactguard.transactguard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMoneyRequest {
    private String sender;
    private String receiver;
    private Double amount;
}
