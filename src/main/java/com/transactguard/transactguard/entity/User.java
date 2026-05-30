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
    private int id;

    private String username;
    private String email;
    private double balance;
    private String password;
    private Role role;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions = new ArrayList<>();

    public void setTransaction (Transaction transaction) {
        this.transactions.add(transaction);

        if (transaction != null) {
            transaction.setUser(this);
        }
    }
}
