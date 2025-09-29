package com.mv.sulworkcafe.repository.jpa;

import com.mv.sulworkcafe.entity.CoffeeEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CoffeeEventRepository extends JpaRepository<CoffeeEvent, Long> {
    Optional<CoffeeEvent> findByEventDate(LocalDate eventDate);
    boolean existsByEventDate(LocalDate eventDate);
}
