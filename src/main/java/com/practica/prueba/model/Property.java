package com.practica.prueba.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String street;

    @Column(name = "monthly_price")
    private java.math.BigDecimal monthlyPrice;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    public Property(String name, String street, java.math.BigDecimal monthlyPrice) {
        this.name = name;
        this.street = street;
        this.monthlyPrice = monthlyPrice;
        this.status = PropertyStatus.AVAILABLE;
    }
}
