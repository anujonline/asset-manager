package com.example.application.data;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double amount;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
    private double rate;

    @CreationTimestamp
    private LocalDateTime localDateTime;
}
