package com.practica.prueba.property.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "properties")
@Getter
@NoArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private BigDecimal monthlyPrice;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    public Property(String name, Address address, BigDecimal monthlyPrice) {
        this.name = name;
        this.address = address;
        this.monthlyPrice = monthlyPrice;
        this.status = PropertyStatus.AVAILABLE;
    }

    public void occupy() {
        if (this.status == PropertyStatus.OCCUPIED) {
            throw new IllegalStateException("La propiedad ya se encuentra ocupada.");
        }
        this.status = PropertyStatus.OCCUPIED;
    }

    public void makeAvailable() {
        this.status = PropertyStatus.AVAILABLE;
    }

    public void update(String name, Address address, BigDecimal monthlyPrice) {
        this.name = name;
        this.address = address;
        this.monthlyPrice = monthlyPrice;
    }
}
