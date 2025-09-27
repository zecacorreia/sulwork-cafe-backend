package com.mv.sulworkcafe.repository.nativequery;

import com.mv.sulworkcafe.entity.CoffeeEvent;
import java.time.LocalDate;
import java.util.Optional;

public interface CoffeeEventNativeRepository {
    CoffeeEvent insert(LocalDate date);
    Optional<CoffeeEvent> findByDateNative(LocalDate date);
}