package com.mv.sulworkcafe.controller;

import com.mv.sulworkcafe.dto.CoffeeEventDTO;
import com.mv.sulworkcafe.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Events")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @Operation(summary = "Criar evento de café (data futura)")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CoffeeEventDTO dto) {
        var saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/events/" + saved.getId()))
                .body(saved);
    }

    @Operation(summary = "Listar eventos (futuros primeiro)")
    @GetMapping
    public List<CoffeeEventDTO> list() {
        return service.listAll();
    }

    @Operation(summary = "Buscar evento por data (YYYY-MM-DD)")
    @GetMapping("/{date}")
    public CoffeeEventDTO findByDate(@PathVariable @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date) {
    return service.findByDate(date);
    }


    @Operation(summary = "Excluir evento por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
