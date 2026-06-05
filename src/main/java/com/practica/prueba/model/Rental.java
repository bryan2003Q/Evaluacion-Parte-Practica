package com.practica.prueba.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "property_id")
    private Long propertyId;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private boolean active;

    public Rental(Long propertyId, Long tenantId) {
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.startDate = LocalDate.now();
        this.active = true;
    }
}
