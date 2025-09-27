package com.mv.sulworkcafe.service;

import com.mv.sulworkcafe.dto.CollaboratorDTO;
import com.mv.sulworkcafe.entity.Collaborator;
import com.mv.sulworkcafe.exception.BusinessException;
import com.mv.sulworkcafe.repository.jpa.CollaboratorRepository;
import com.mv.sulworkcafe.repository.nativequery.CollaboratorNativeRepository;
import com.mv.sulworkcafe.util.CpfValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollaboratorService {
    private final CollaboratorRepository repo;
    private final CollaboratorNativeRepository nativeRepo;
    public CollaboratorService(CollaboratorRepository r, CollaboratorNativeRepository n){ this.repo=r; this.nativeRepo=n; }

    @Transactional
    public Collaborator create(CollaboratorDTO dto){
        String cpf = CpfValidator.onlyDigits(dto.cpf());
        if (!CpfValidator.isValid(cpf)) throw new BusinessException("CPF inválido (11 dígitos e DV)");
        repo.findByCpf(cpf).ifPresent(c -> { throw new BusinessException("CPF já cadastrado"); });
        return nativeRepo.insert(dto.name().trim(), cpf);
    }
}