package com.mv.sulworkcafe.repository.jpa;

import com.mv.sulworkcafe.entity.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    Optional<Collaborator> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
