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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ItemService {

    private final CoffeeEventRepository eventRepo;
    private final CollaboratorRepository collabRepo;
    private final CoffeeItemNativeRepository nativeRepo;
    private final CoffeeItemRepository itemJpaRepo;

    public ItemService(CoffeeEventRepository eventRepo,
                       CollaboratorRepository collabRepo,
                       CoffeeItemNativeRepository nativeRepo,
                       CoffeeItemRepository itemJpaRepo) {
        this.eventRepo = eventRepo;
        this.collabRepo = collabRepo;
        this.nativeRepo = nativeRepo;
        this.itemJpaRepo = itemJpaRepo;
    }

    @Transactional
    public CoffeeItemDTO create(CoffeeItemCreateDTO dto) {
        LocalDate date = dto.eventDate();
        CoffeeEvent event = eventRepo.findByEventDate(date)
                .orElseThrow(() -> new NotFoundException("Data do café não encontrada: " + date));

        var collaborator = collabRepo.findByCpf(dto.cpf())
                .orElseThrow(() -> new NotFoundException("Colaborador não encontrado para o CPF informado"));

        String itemName = dto.itemName().trim();
        boolean itemExists = itemJpaRepo.existsByEventAndItemNameIgnoreCase(event, itemName);
        if (itemExists) {
            throw new BusinessException("O item '" + itemName + "' já foi escolhido para esta data.");
        }

        CoffeeItem item = nativeRepo.insert(event.getId(), collaborator.getId(), itemName);
        return toDTO(item);
    }

    @Transactional
    public CoffeeItemDTO mark(long id, Boolean brought) {
        CoffeeItem updated = nativeRepo.mark(id, brought);
        return toDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<CoffeeItemDTO> listByDate(LocalDate date) {
        return nativeRepo.listByDate(date).stream().map(this::toDTO).toList();
    }

    @Transactional
    public void delete(long id) {
        var deleted = nativeRepo.deleteByIdReturning(id);
        if (deleted.isEmpty()) {
            throw new NotFoundException("Item não encontrado");
        }
    }

    private CoffeeItemDTO toDTO(CoffeeItem i) {
        return new CoffeeItemDTO(
                i.getId(),
                i.getEvent().getEventDate().toString(),
                i.getCollaborator().getId(),
                i.getCollaborator().getName(),
                i.getCollaborator().getCpf(),
                i.getItemName(),
                i.getBrought()
        );
    }
}
