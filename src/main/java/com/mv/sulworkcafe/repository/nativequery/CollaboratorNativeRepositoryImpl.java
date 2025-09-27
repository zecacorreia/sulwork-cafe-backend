package com.mv.sulworkcafe.repository.nativequery;

import com.mv.sulworkcafe.entity.Collaborator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public class CollaboratorNativeRepositoryImpl implements CollaboratorNativeRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collaborator insert(String name, String cpf) {
        String sql = """
            INSERT INTO collaborator(name, cpf)
            VALUES (:name, :cpf)
            RETURNING *
        """;
        return (Collaborator) em.createNativeQuery(sql, Collaborator.class)
                .setParameter("name", name)
                .setParameter("cpf", cpf)
                .getSingleResult();
    }

    @Override
    public Optional<Collaborator> findByCpfNative(String cpf) {
        String sql = "SELECT * FROM collaborator WHERE cpf = :cpf";
        var list = em.createNativeQuery(sql, Collaborator.class)
                .setParameter("cpf", cpf)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of((Collaborator) list.get(0));
    }

    @Override
    public Optional<Collaborator> deleteByIdReturning(long id) {
        String sql = "DELETE FROM collaborator WHERE id = :id RETURNING *";
        var list = em.createNativeQuery(sql, Collaborator.class)
                .setParameter("id", id)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of((Collaborator) list.get(0));
    }
}
