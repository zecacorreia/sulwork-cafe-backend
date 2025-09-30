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

import java.time.LocalDate;
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

        var name = req.name() == null ? "" : req.name().trim();
        if (name.isBlank()) throw new BusinessException("Nome é obrigatório");

        var cpfRaw = req.cpf();
        if (cpfRaw == null) throw new BusinessException("CPF é obrigatório");
        var cpf = CpfValidator.normalize(cpfRaw);
        if (!CpfValidator.isValid(cpf)) throw new BusinessException("CPF inválido (11 dígitos)");

        LocalDate date = req.breakfastDate();
        if (date == null) throw new BusinessException("Data do café é obrigatória");

        if (!date.isAfter(LocalDate.now())) {
            throw new BusinessException("A data do café deve ser maior que a data atual");
        }

        if (req.items() == null || req.items().isEmpty())
            throw new BusinessException("Informe ao menos um item");

        var normalizedItems = req.items().stream()
                .map(it -> {
                    var n = (it == null || it.name() == null) ? "" : it.name().trim();
                    if (n.isBlank()) throw new BusinessException("Item inválido");
                    return new ItemPair(n, it.brought());
                })
                .toList();

        var seen = new java.util.TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        for (var it : normalizedItems) {
            if (!seen.add(it.name())) {
                throw new BusinessException("Item duplicado no envio: '" + it.name() + "'");
            }
        }

        if (collaboratorRepo.existsByCpf(cpf)) {
            throw new BusinessException("Já existe colaborador com este CPF");
        }

        var collab = collaboratorRepo.save(Collaborator.builder().name(name).cpf(cpf).build());

        var event = eventRepo.findByEventDate(date)
                .orElseGet(() -> eventRepo.save(
                        CoffeeEvent.builder().eventDate(date).build()
                ));

        for (var it : normalizedItems) {
            if (itemRepo.existsByEventAndItemNameIgnoreCase(event, it.name())) {
                throw new BusinessException("Item '" + it.name() + "' já escolhido para esta data");
            }
        }

        var toSave = normalizedItems.stream()
                .map(it -> CoffeeItem.builder()
                        .itemName(it.name())
                        .brought(Boolean.TRUE.equals(it.brought()))
                        .event(event)
                        .collaborator(collab)
                        .build())
                .toList();
        itemRepo.saveAll(toSave);

        var names = toSave.stream().map(CoffeeItem::getItemName).toList();
        return new ParticipantResponse(collab.getId(), collab.getName(), collab.getCpf(), event.getEventDate(), names);
    }

    private record ItemPair(String name, Boolean brought) {}
}
