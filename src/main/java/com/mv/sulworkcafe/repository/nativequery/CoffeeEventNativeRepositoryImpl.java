package com.mv.sulworkcafe.repository.nativequery;

import com.mv.sulworkcafe.entity.CoffeeEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@Transactional
public class CoffeeEventNativeRepositoryImpl implements CoffeeEventNativeRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public CoffeeEvent insert(LocalDate date) {
        String sql = """
            INSERT INTO coffee_event(event_date)
            VALUES (:date)
            RETURNING *
        """;
        return (CoffeeEvent) em.createNativeQuery(sql, CoffeeEvent.class)
                .setParameter("date", date)
                .getSingleResult();
    }

    @Override
    public Optional<CoffeeEvent> findByDateNative(LocalDate date) {
        String sql = "SELECT * FROM coffee_event WHERE event_date = :date";
        var list = em.createNativeQuery(sql, CoffeeEvent.class)
                .setParameter("date", date)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of((CoffeeEvent) list.get(0));
    }
}