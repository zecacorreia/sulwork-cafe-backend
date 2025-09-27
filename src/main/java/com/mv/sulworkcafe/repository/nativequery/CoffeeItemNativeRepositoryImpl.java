package com.mv.sulworkcafe.repository.nativequery;

import com.mv.sulworkcafe.entity.CoffeeItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CoffeeItemNativeRepositoryImpl implements CoffeeItemNativeRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public CoffeeItem insert(long eventId, long collaboratorId, String itemName) {
        String sql = """
            INSERT INTO coffee_item(event_id, collaborator_id, item_name)
            VALUES (:e, :c, :n)
            RETURNING *
        """;
        return (CoffeeItem) em.createNativeQuery(sql, CoffeeItem.class)
                .setParameter("e", eventId)
                .setParameter("c", collaboratorId)
                .setParameter("n", itemName)
                .getSingleResult();
    }

    @Override
    public CoffeeItem mark(long id, Boolean status) {
        String sql = """
            UPDATE coffee_item
               SET brought = :b, checked_at = now()
             WHERE id = :id
         RETURNING *
        """;
        return (CoffeeItem) em.createNativeQuery(sql, CoffeeItem.class)
                .setParameter("b", status)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<CoffeeItem> listByDate(LocalDate date) {
        String sql = """
            SELECT ci.* FROM coffee_item ci
            JOIN coffee_event e ON e.id = ci.event_id
            WHERE e.event_date = :d
            ORDER BY lower(ci.item_name)
        """;
        return em.createNativeQuery(sql, CoffeeItem.class)
                .setParameter("d", date)
                .getResultList();
    }

    @Override
    public Optional<CoffeeItem> deleteByIdReturning(long id) {
        String sql = "DELETE FROM coffee_item WHERE id = :id RETURNING *";
        var list = em.createNativeQuery(sql, CoffeeItem.class)
                .setParameter("id", id)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of((CoffeeItem) list.get(0));
    }
}
