package com.example.application.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Entity
@Data
@Accessors(chain = true)
public class Account implements Comparable<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String displayName;
    private String label;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;


    @Override
    public int compareTo( Integer o) {
        return o.compareTo(this.id);
    }
}
