package com.mv.sulworkcafe.service;

import com.mv.sulworkcafe.dto.CoffeeItemCreateDTO;
import com.mv.sulworkcafe.dto.CoffeeItemDTO;
import com.mv.sulworkcafe.entity.CoffeeEvent;
import com.mv.sulworkcafe.entity.CoffeeItem;
import com.mv.sulworkcafe.exception.BusinessException;
import com.mv.sulworkcafe.exception.NotFoundException;
import com.mv.sulworkcafe.repository.jpa.CoffeeEventRepository;
import com.mv.sulworkcafe.repository.jpa.CoffeeItemRepository;
import com.mv.sulworkcafe.repository.jpa.CollaboratorRepository;
import com.mv.sulworkcafe.repository.nativequery.CoffeeItemNativeRepository;
import com.mv.sulworkcafe.util.CpfValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ItemService {

    private final CoffeeEventRepository eventRepo;
    private final CollaboratorRepository collabRepo;
    private final CoffeeItemRepository itemJpaRepo;
    private final CoffeeItemNativeRepository nativeRepo;

    public ItemService(CoffeeEventRepository eventRepo,
                       CollaboratorRepository collabRepo,
                       CoffeeItemRepository itemJpaRepo,
                       CoffeeItemNativeRepository nativeRepo) {
        this.eventRepo = eventRepo;
        this.collabRepo = collabRepo;
        this.itemJpaRepo = itemJpaRepo;
        this.nativeRepo = nativeRepo;
    }

    @Transactional
    public CoffeeItemDTO create(CoffeeItemCreateDTO dto) {
        LocalDate date = dto.eventDate();
        CoffeeEvent event = eventRepo.findByEventDate(date)
                .orElseThrow(() -> new NotFoundException("Data do café não encontrada: " + date));

        var cpf = CpfValidator.normalize(dto.cpf());
        var collaborator = collabRepo.findByCpf(cpf)
                .orElseThrow(() -> new NotFoundException("Colaborador não encontrado para o CPF informado"));

        String itemName = dto.itemName() == null ? "" : dto.itemName().trim();
        if (itemName.isBlank()) throw new BusinessException("Nome do item é obrigatório");

        if (itemJpaRepo.existsByEventAndItemNameIgnoreCase(event, itemName)) {
            throw new BusinessException("O item '" + itemName + "' já foi escolhido para esta data.");
        }

        CoffeeItem saved = itemJpaRepo.save(
                CoffeeItem.builder()
                        .event(event)
                        .collaborator(collaborator)
                        .itemName(itemName)
                        .brought(false)
                        .build()
        );
        return toDTO(saved);
    }

    @Transactional
    public CoffeeItemDTO mark(long id, Boolean brought) {
        if (brought == null) brought = false;

        var ci = itemJpaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Item não encontrado"));
        ci.setBrought(brought);
        var saved = itemJpaRepo.save(ci);

        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CoffeeItemDTO> listByDate(LocalDate date) {
        return nativeRepo.listByDate(date).stream().map(this::toDTO).toList();
    }

    @Transactional
    public void delete(long id) {
        if (!itemJpaRepo.existsById(id)) {
            throw new NotFoundException("Item não encontrado");
        }
        itemJpaRepo.deleteById(id);
    }

    private CoffeeItemDTO toDTO(CoffeeItem i) {
        return new CoffeeItemDTO(
                i.getId(),
                i.getEvent().getEventDate().toString(),
                i.getCollaborator().getId(),
                i.getCollaborator().getName(),
                i.getCollaborator().getCpf(),
                i.getItemName(),
                Boolean.TRUE.equals(i.getBrought())
        );
    }
}
