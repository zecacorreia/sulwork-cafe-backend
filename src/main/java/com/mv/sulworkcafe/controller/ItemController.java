package com.mv.sulworkcafe.controller;

import com.mv.sulworkcafe.dto.CoffeeItemCreateDTO;
import com.mv.sulworkcafe.dto.CoffeeItemDTO;
import com.mv.sulworkcafe.dto.MarkItemDTO;
import com.mv.sulworkcafe.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Items")
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar item para o café (CPF + data + item)")
    @PostMapping
    public CoffeeItemDTO create(@Valid @RequestBody CoffeeItemCreateDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Listar itens por data do evento (YYYY-MM-DD)")
    @GetMapping("/by-date/{date}")
    public List<CoffeeItemDTO> listByDate(@PathVariable("date") @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
        return service.listByDate(date);
    }

    @Operation(summary = "Marcar se o colaborador trouxe ou não a opção")
    @PatchMapping("/{id}/mark")
    public CoffeeItemDTO mark(@PathVariable("id") long id, @Valid @RequestBody MarkItemDTO dto) {
        return service.mark(id, dto.brought());
    }

    @Operation(summary = "Excluir item por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
