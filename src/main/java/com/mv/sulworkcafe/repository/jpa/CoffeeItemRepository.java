package com.mv.sulworkcafe.repository.jpa;

import com.mv.sulworkcafe.entity.CoffeeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CoffeeItemRepository extends JpaRepository<CoffeeItem, Long> {
    @Query("SELECT i FROM CoffeeItem i JOIN FETCH i.collaborator WHERE i.event.eventDate = :date")
    List<CoffeeItem> findByEventDateWithCollaborator(@Param("date") String date);
}