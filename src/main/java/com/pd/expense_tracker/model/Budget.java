package com.pd.expense_tracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double maxLimit;

    @ManyToOne
    @JoinColumn(name = "userIdentifier", nullable = false)
    private UserIdentifier user;
}
