package com.mv.sulworkcafe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CoffeeItemCreateDTO(
        @Schema(example = "2025-10-01") @NotNull java.time.LocalDate eventDate,
        @Schema(example = "12478617109") @NotBlank String cpf,
        @Schema(example = "Suco de Acerola") @NotBlank String itemName
) {}
