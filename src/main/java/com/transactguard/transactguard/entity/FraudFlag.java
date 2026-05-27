package com.transactguard.transactguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FraudFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    Transaction transaction;
    String reason;
    Boolean resolved;
    Date createdAt;
}
