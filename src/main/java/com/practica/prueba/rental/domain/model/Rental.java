package com.practica.prueba.rental.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Getter
@NoArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long propertyId;
    private Long tenantId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    public Rental(Long propertyId, Long tenantId) {
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.startDate = LocalDate.now();
        this.active = true;
    }

    public void finish() {
        if (!this.active) {
            throw new IllegalStateException("El arriendo ya se encuentra finalizado.");
        }
        this.active = false;
        this.endDate = LocalDate.now();
    }
}
