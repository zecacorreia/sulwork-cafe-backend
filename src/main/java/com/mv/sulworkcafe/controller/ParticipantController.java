package com.mv.sulworkcafe.controller;

import com.mv.sulworkcafe.api.dto.ParticipantResponse;
import com.mv.sulworkcafe.api.dto.ParticipantUpsertRequest;
import com.mv.sulworkcafe.service.ParticipantFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Participants")
@RestController
@RequestMapping("/api/participants")
@CrossOrigin(origins = "http://localhost:4000, https://sulwork-cafe-frontend-production.up.railway.app")
public class ParticipantController {

    private final ParticipantFacadeService service;

    public ParticipantController(ParticipantFacadeService service) {
        this.service = service;
    }

    @Operation(summary = "Criar/atualizar participante com itens do café")
    @PostMapping
    public ResponseEntity<ParticipantResponse> create(@RequestBody ParticipantUpsertRequest req) {
        var resp = service.createOrUpdateParticipantWithBreakfast(req);
        return ResponseEntity.status(201).body(resp);
    }
}
