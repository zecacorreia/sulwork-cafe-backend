package com.mv.sulworkcafe.api.dto;

import java.time.LocalDate;
import java.util.List;

public record ParticipantUpsertRequest(
        String name,
        String cpf,
        LocalDate breakfastDate,
        List<ItemReq> items
) {
    public record ItemReq(String name, boolean brought) {}
}
