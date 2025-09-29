package com.mv.sulworkcafe.service;

import com.mv.sulworkcafe.dto.CoffeeEventDTO;
import com.mv.sulworkcafe.entity.CoffeeEvent;
import com.mv.sulworkcafe.exception.BusinessException;
import com.mv.sulworkcafe.exception.NotFoundException;
import com.mv.sulworkcafe.repository.jpa.CoffeeEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class EventService {

    private final CoffeeEventRepository jpaRepo;

    public EventService(CoffeeEventRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Transactional
    public CoffeeEvent create(CoffeeEventDTO dto) {
        if (dto == null || dto.eventDate() == null) {
            throw new BusinessException("Data do evento é obrigatória");
        }

        LocalDate date = dto.eventDate();

        // Regra: precisa ser estritamente maior que hoje
        if (!date.isAfter(LocalDate.now())) {
            throw new BusinessException("A data do café deve ser maior que a data atual");
        }

        if (jpaRepo.existsByEventDate(date)) {
            throw new BusinessException("Já existe um café nesta data");
        }

        CoffeeEvent entity = CoffeeEvent.builder()
                .eventDate(date)
                .build();

        return jpaRepo.save(entity);
    }

    @Transactional(readOnly = true)
    public List<CoffeeEventDTO> listAll() {
        return jpaRepo.findAll().stream()
                .sorted(Comparator.comparing(CoffeeEvent::getEventDate))
                .map(e -> new CoffeeEventDTO(e.getEventDate()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CoffeeEventDTO findByDate(LocalDate date) {
        if (date == null) {
            throw new BusinessException("Data do café é obrigatória");
        }
        CoffeeEvent e = jpaRepo.findByEventDate(date)
                .orElseThrow(() -> new NotFoundException("Data do café não encontrada: " + date));
        return new CoffeeEventDTO(e.getEventDate());
    }

    @Transactional
    public void delete(long id) {
        if (!jpaRepo.existsById(id)) {
            throw new NotFoundException("Evento não encontrado");
        }
        jpaRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CoffeeEvent getEntityByDate(LocalDate date) {
        if (date == null) {
            throw new BusinessException("Data do café é obrigatória");
        }
        return jpaRepo.findByEventDate(date)
                .orElseThrow(() -> new NotFoundException("Data do café não encontrada: " + date));
    }
}
