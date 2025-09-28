package com.mv.sulworkcafe.service;

import com.mv.sulworkcafe.dto.CollaboratorDTO;
import com.mv.sulworkcafe.entity.Collaborator;
import com.mv.sulworkcafe.exception.BusinessException;
import com.mv.sulworkcafe.exception.NotFoundException;
import com.mv.sulworkcafe.repository.jpa.CollaboratorRepository;
import com.mv.sulworkcafe.repository.nativequery.CollaboratorNativeRepository;
import com.mv.sulworkcafe.util.CpfValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollaboratorService {

    private final CollaboratorRepository jpaRepo;
    private final CollaboratorNativeRepository nativeRepo;

    public CollaboratorService(CollaboratorRepository jpaRepo,
                               CollaboratorNativeRepository nativeRepo) {
        this.jpaRepo = jpaRepo;
        this.nativeRepo = nativeRepo;
    }

    @Transactional
    public Collaborator create(CollaboratorDTO dto) {
        if (dto == null || dto.name() == null || dto.name().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        String cpf = dto.cpf();
        if (!CpfValidator.isValid(cpf)) {
            throw new BusinessException("CPF inválido (11 dígitos)");
        }
        if (jpaRepo.findByCpf(cpf).isPresent()) {
            throw new BusinessException("CPF já cadastrado");
        }
        return nativeRepo.insert(dto.name().trim(), cpf);
    }

    @Transactional(readOnly = true)
    public List<CollaboratorDTO> listAll() {
        return jpaRepo.findAll()
                .stream()
                .map(c -> new CollaboratorDTO(c.getName(), c.getCpf()))
                .toList();
    }

    @Transactional
    public void delete(long id) {
        if (!jpaRepo.existsById(id)) {
            throw new NotFoundException("Colaborador não encontrado");
        }
        jpaRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Collaborator findEntityByCpf(String cpf) {
        return jpaRepo.findByCpf(cpf)
                .orElseThrow(() -> new NotFoundException("Colaborador não encontrado para o CPF informado"));
    }

    @Transactional
    public Collaborator update(Long id, CollaboratorDTO dto) {
        Collaborator existingCollaborator = jpaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Colaborador com ID " + id + " não encontrado."));

        if (dto.name() == null || dto.name().isBlank()) {
            throw new BusinessException("O nome não pode ser vazio.");
        }
        existingCollaborator.setName(dto.name().trim());

        return jpaRepo.save(existingCollaborator);
    }
}
