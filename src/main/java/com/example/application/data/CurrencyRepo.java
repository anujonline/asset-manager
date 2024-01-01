package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepo extends JpaRepository<Currency, Integer> {
    Optional<Currency> findByDisplayName(String displayName);

    boolean existsByDisplayName(String displayName);
}
