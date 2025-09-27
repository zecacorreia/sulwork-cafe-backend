package com.mv.sulworkcafe.repository.nativequery;

import com.mv.sulworkcafe.entity.CoffeeItem;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CoffeeItemNativeRepository {
    CoffeeItem insert(long eventId, long collaboratorId, String itemName);
    CoffeeItem mark(long id, Boolean status);
    List<CoffeeItem> listByDate(LocalDate date);
    Optional<CoffeeItem> deleteByIdReturning(long id);
}