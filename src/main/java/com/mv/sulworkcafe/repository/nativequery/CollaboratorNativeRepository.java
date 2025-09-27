package com.mv.sulworkcafe.repository.nativequery;

import com.mv.sulworkcafe.entity.Collaborator;
import java.util.Optional;

public interface CollaboratorNativeRepository {
    Collaborator insert(String name, String cpf);
    Optional<Collaborator> findByCpfNative(String cpf);
    Optional<Collaborator> deleteByIdReturning(long id);
}
