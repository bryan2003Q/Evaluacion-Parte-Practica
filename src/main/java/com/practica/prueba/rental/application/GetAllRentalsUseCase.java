package com.practica.prueba.rental.application;

import com.practica.prueba.rental.domain.model.Rental;
import com.practica.prueba.rental.infrastructure.persistence.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllRentalsUseCase {
    private final RentalRepository rentalRepository;

    @Transactional(readOnly = true)
    public List<Rental> execute() {
        return rentalRepository.findAll();
    }
}
