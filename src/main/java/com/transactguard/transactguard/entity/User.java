package com.transactguard.transactguard.entity;

import com.transactguard.transactguard.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;
    private Double balance;
    private String password;
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "sender")
    private List<Transaction> senderTransactions = new ArrayList<>();
    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receiverTransactions = new ArrayList<>();

    public void setSenderTransactions (Transaction senderTransactions) {
        this.senderTransactions.add(senderTransactions);

        if (senderTransactions != null) {
            senderTransactions.setSender(this);
        }
    }

    public void setReceiverTransactions (Transaction receiverTransactions) {
        this.receiverTransactions.add(receiverTransactions);

        if (receiverTransactions != null) {
            receiverTransactions.setReceiver(this);
        }
    }

}
