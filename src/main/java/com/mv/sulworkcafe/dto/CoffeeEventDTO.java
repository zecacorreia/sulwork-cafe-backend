package com.mv.sulworkcafe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CoffeeEventDTO(
        @Schema(example = "2025-10-01") @NotNull LocalDate eventDate
) {}