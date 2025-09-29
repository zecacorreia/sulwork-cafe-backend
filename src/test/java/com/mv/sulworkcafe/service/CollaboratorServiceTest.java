package com.mv.sulworkcafe.service;

import com.mv.sulworkcafe.dto.CollaboratorDTO;
import com.mv.sulworkcafe.entity.Collaborator;
import com.mv.sulworkcafe.repository.jpa.CollaboratorRepository;
import com.mv.sulworkcafe.repository.nativequery.CollaboratorNativeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CollaboratorServiceTest {

    CollaboratorRepository jpaRepo = mock(CollaboratorRepository.class);
    CollaboratorNativeRepository nativeRepo = mock(CollaboratorNativeRepository.class);
    CollaboratorService service;

    @BeforeEach
    void setUp() {
        service = new CollaboratorService(jpaRepo, nativeRepo);
    }

    @Test
    void create_acceptsCpfWithLeadingZero_andPersistsNormalized() {
        var dto = new CollaboratorDTO("Fulano", "068.759.310-70");

        when(jpaRepo.findByCpf(anyString())).thenReturn(Optional.empty());

        when(jpaRepo.save(any(Collaborator.class)))
                .thenAnswer(inv -> {
                    var c = inv.getArgument(0, Collaborator.class);
                    c.setId(1L);        // simula PK gerada
                    return c;
                });

        var created = service.create(dto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getCpf()).isEqualTo("06875931070");

        verify(jpaRepo).findByCpf("06875931070");
        verify(jpaRepo).save(argThat(c ->
                "Fulano".equals(c.getName()) && "06875931070".equals(c.getCpf())
        ));
        // se não usa mais nativo:
        verifyNoInteractions(nativeRepo);
    }

    @Test
    void create_rejectsDuplicatedCpf() {
        when(jpaRepo.findByCpf("06875931070")).thenReturn(Optional.of(new Collaborator()));

        var dto = new CollaboratorDTO("Fulano", "06875931070");

        assertThatThrownBy(() -> service.create(dto))
                .hasMessageContaining("CPF já cadastrado");

        verifyNoInteractions(nativeRepo);
    }
}
