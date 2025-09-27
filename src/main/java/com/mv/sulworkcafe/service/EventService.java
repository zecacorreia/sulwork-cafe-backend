package com.mv.sulworkcafe.service;

import com.mv.sulworkcafe.dto.CoffeeEventDTO;
import com.mv.sulworkcafe.entity.CoffeeEvent;
import com.mv.sulworkcafe.exception.BusinessException;
import com.mv.sulworkcafe.repository.jpa.CoffeeEventRepository;
import com.mv.sulworkcafe.repository.nativequery.CoffeeEventNativeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class EventService {
    private final CoffeeEventRepository repo;
    private final CoffeeEventNativeRepository nativeRepo;
    public EventService(CoffeeEventRepository r, CoffeeEventNativeRepository n){ this.repo=r; this.nativeRepo=n; }


    @Transactional
    public CoffeeEvent create(CoffeeEventDTO dto){
        LocalDate d = dto.eventDate();
        if (!d.isAfter(LocalDate.now())) throw new BusinessException("Data do café deve ser maior que a data atual");
        repo.findByEventDate(d).ifPresent(e -> { throw new BusinessException("Já existe café nessa data"); });
        return nativeRepo.insert(d);
    }
}
