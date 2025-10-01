package com.mv.sulworkcafe.controller;

import com.mv.sulworkcafe.dto.CollaboratorDTO;
import com.mv.sulworkcafe.service.CollaboratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Collaborators")
@RestController
@RequestMapping("/api/collaborators")
@CrossOrigin(origins = "http://localhost:4000, https://sulwork-cafe-frontend-production.up.railway.app")
public class CollaboratorController {

    private final CollaboratorService service;

    public CollaboratorController(CollaboratorService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar colaborador")
    @PostMapping
    public ResponseEntity<CollaboratorDTO> create(@Valid @RequestBody CollaboratorDTO dto) {
        var saved = service.create(dto);
        var body = new CollaboratorDTO(saved.getName(), saved.getCpf());
        return ResponseEntity.created(URI.create("/api/collaborators/" + saved.getId()))
                .body(body);
    }

    @Operation(summary = "Atualizar colaborador por ID")
    @PutMapping("/{id}")
    public ResponseEntity<CollaboratorDTO> update(@PathVariable("id") long id, @Valid @RequestBody CollaboratorDTO dto) {
        var updated = service.update(id, dto);
        var body = new CollaboratorDTO(updated.getName(), updated.getCpf());
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Listar colaboradores")
    @GetMapping
    public List<CollaboratorDTO> list() {
        return service.listAll();
    }

    @Operation(summary = "Excluir colaborador por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}