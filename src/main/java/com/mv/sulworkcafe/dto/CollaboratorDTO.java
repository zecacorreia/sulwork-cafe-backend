package com.mv.sulworkcafe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CollaboratorDTO(
        @Schema(example = "Fulano de Tal") @NotBlank String name,
        @Schema(example = "12478617109") @Pattern(regexp = "^[0-9]{11}$") String cpf
) {}