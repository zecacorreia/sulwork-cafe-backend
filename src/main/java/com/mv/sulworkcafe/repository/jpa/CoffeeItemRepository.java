package com.mv.sulworkcafe.repository.jpa;

import com.mv.sulworkcafe.entity.CoffeeEvent;
import com.mv.sulworkcafe.entity.CoffeeItem;
import com.mv.sulworkcafe.entity.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeItemRepository extends JpaRepository<CoffeeItem, Long> {
    boolean existsByEventAndItemNameIgnoreCase(CoffeeEvent event, String itemName);
    boolean existsByEventAndCollaborator(CoffeeEvent event, Collaborator collaborator); // <-- novo
}