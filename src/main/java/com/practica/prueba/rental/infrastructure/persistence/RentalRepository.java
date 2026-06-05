package com.practica.prueba.rental.infrastructure.persistence;

import com.practica.prueba.rental.domain.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByActiveTrue();
}
