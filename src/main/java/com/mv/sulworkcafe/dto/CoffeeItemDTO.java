package com.mv.sulworkcafe.dto;

public record CoffeeItemDTO(
        Long id,
        String eventDate,
        String collaboratorName,
        String cpf,
        String itemName,
        Boolean brought
) {}