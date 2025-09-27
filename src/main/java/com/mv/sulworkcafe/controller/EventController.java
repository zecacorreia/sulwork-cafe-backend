package com.mv.sulworkcafe.controller;

import com.mv.sulworkcafe.dto.CoffeeEventDTO;
import com.mv.sulworkcafe.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

import java.net.URI;
import java.time.LocalDate;
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
    public ResponseEntity<CoffeeEventDTO> create(@Valid @RequestBody CoffeeEventDTO dto) {
        var saved = service.create(dto);
        var body = new CoffeeEventDTO(saved.getEventDate());
        return ResponseEntity.created(URI.create("/api/events/" + saved.getId()))
                .body(body);
    }


    @Operation(summary = "Listar eventos (futuros primeiro)")
    @GetMapping
    public List<CoffeeEventDTO> list() {
        return service.listAll();
    }

    @Operation(summary = "Buscar evento por data (YYYY-MM-DD)")
    @GetMapping("/{date}")
    public CoffeeEventDTO findByDate(@PathVariable("date") @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
        return service.findByDate(date);
    }

    @Operation(summary = "Excluir evento por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
