package com.pd.expense_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "UserIdentifier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userIdentifier;

    private String name;
}
