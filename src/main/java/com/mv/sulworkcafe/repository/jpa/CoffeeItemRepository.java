package com.mv.sulworkcafe.repository.jpa;

import com.mv.sulworkcafe.entity.CoffeeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeItemRepository extends JpaRepository<CoffeeItem, Long> {}
