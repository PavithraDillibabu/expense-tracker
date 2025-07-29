package com.pd.expense_tracker.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents an expense record")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userIdentifier", nullable = false)
    @Schema(description = "User who made the expense")
    private UserIdentifier user;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Expense description", example = "Monthly groceries")
    private String description;

    @NotNull
    @Positive
    @Schema(description = "Amount spent or received", example = "100.50")
    private Double amount;

    @NotNull
    @Schema(description = "Date of the transaction", example = "2025-07-25")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "Expense category")
    @JsonBackReference
    private Category category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of transaction", example = "DEBIT")
    private ExpenseType type;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "Expense{id=" + id + ", description='" + description + "', amount=" + amount + "}";
    }
}