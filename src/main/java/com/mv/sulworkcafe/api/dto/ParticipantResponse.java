package com.mv.sulworkcafe.api.dto;

import java.time.LocalDate;
import java.util.List;

public record ParticipantResponse(
        Long collaboratorId,
        String name,
        String cpf,
        LocalDate breakfastDate,
        List<String> items
) {}