package com.transactguard.transactguard.entity;

import com.transactguard.transactguard.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sender;
    private String receiver;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalTime timestamp;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}
