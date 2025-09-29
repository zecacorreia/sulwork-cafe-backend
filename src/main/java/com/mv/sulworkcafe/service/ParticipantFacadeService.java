package com.mv.sulworkcafe.service;

import com.mv.sulworkcafe.api.dto.ParticipantResponse;
import com.mv.sulworkcafe.api.dto.ParticipantUpsertRequest;
import com.mv.sulworkcafe.entity.CoffeeEvent;
import com.mv.sulworkcafe.entity.CoffeeItem;
import com.mv.sulworkcafe.entity.Collaborator;
import com.mv.sulworkcafe.exception.BusinessException;
import com.mv.sulworkcafe.repository.jpa.CoffeeEventRepository;
import com.mv.sulworkcafe.repository.jpa.CoffeeItemRepository;
import com.mv.sulworkcafe.repository.jpa.CollaboratorRepository;
import com.mv.sulworkcafe.util.CpfValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ParticipantFacadeService {

    private final CollaboratorRepository collaboratorRepo;
    private final CoffeeEventRepository eventRepo;
    private final CoffeeItemRepository itemRepo;

    public ParticipantFacadeService(CollaboratorRepository collaboratorRepo,
                                    CoffeeEventRepository eventRepo,
                                    CoffeeItemRepository itemRepo) {
        this.collaboratorRepo = collaboratorRepo;
        this.eventRepo = eventRepo;
        this.itemRepo = itemRepo;
    }

    @Transactional
    public ParticipantResponse createOrUpdateParticipantWithBreakfast(ParticipantUpsertRequest req) {
        if (req == null) throw new BusinessException("Payload obrigatório");

        var name = (req.name() == null ? "" : req.name().trim());
        if (name.isBlank()) throw new BusinessException("Nome é obrigatório");

        var cpf = CpfValidator.normalize(req.cpf());
        if (!CpfValidator.isValid(cpf)) throw new BusinessException("CPF inválido (11 dígitos)");

        if (req.breakfastDate() == null) throw new BusinessException("Data do café é obrigatória");
        if (req.items() == null || req.items().isEmpty()) throw new BusinessException("Informe ao menos um item");

        // colaborador (cria ou atualiza nome)
        var collab = collaboratorRepo.findByCpf(cpf)
                .orElseGet(() -> collaboratorRepo.save(Collaborator.builder().name(name).cpf(cpf).build()));
        if (!Objects.equals(name, collab.getName())) {
            collab.setName(name);
            collaboratorRepo.save(collab);
        }

        var event = eventRepo.findByEventDate(req.breakfastDate())
                .orElseGet(() -> eventRepo.save(CoffeeEvent.builder().eventDate(req.breakfastDate()).build()));

        for (var it : req.items()) {
            var itemName = it == null || it.name() == null ? "" : it.name().trim();
            if (itemName.isBlank()) {
                throw new BusinessException("Item inválido");
            }
            if (itemRepo.existsByEventAndNameIgnoreCase(event, itemName)) {
                throw new BusinessException("Item '" + itemName + "' já escolhido para esta data");
            }
        }

        // grava itens
        for (var it : req.items()) {
            itemRepo.save(
                    CoffeeItem.builder()
                            .itemName(it.name().trim())
                            .brought(Boolean.TRUE.equals(it.brought()))
                            .event(event)
                            .collaborator(collab)
                            .build()
            );
        }

        var names = req.items().stream().map(i -> i.name().trim()).toList();
        return new ParticipantResponse(collab.getId(), collab.getName(), collab.getCpf(), event.getEventDate(), names);
    }
}
