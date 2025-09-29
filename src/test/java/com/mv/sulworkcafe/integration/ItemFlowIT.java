package com.mv.sulworkcafe.integration;

import com.mv.sulworkcafe.dto.CoffeeEventDTO;
import com.mv.sulworkcafe.dto.CoffeeItemCreateDTO;
import com.mv.sulworkcafe.dto.CoffeeItemDTO;
import com.mv.sulworkcafe.dto.CollaboratorDTO;
import com.mv.sulworkcafe.service.CollaboratorService;
import com.mv.sulworkcafe.service.EventService;
import com.mv.sulworkcafe.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ItemFlowIT {

    @Container
    static PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("sulwork_cafe_it")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.flyway.url", POSTGRES::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired CollaboratorService collaboratorService;
    @Autowired EventService eventService;
    @Autowired ItemService itemService;

    @Test
    @DisplayName("Fluxo completo: colaborador → evento → item → listar → marcar")
    void endToEndFlow() {
        final String validCpf = "06875931070"; // CPF Válido!
        var collab = collaboratorService.create(new CollaboratorDTO("Fulano de Tal", validCpf));
        assertThat(collab.getId()).isNotNull();
        LocalDate eventDate = LocalDate.now().plusDays(5);
        var event = eventService.create(new CoffeeEventDTO(eventDate));
        assertThat(event.getId()).isNotNull();
        CoffeeItemDTO item = itemService.create(new CoffeeItemCreateDTO(eventDate, validCpf, "Suco de Acerola"));
        assertThat(item.id()).isNotNull();
        assertThat(item.brought()).isFalse();
        List<CoffeeItemDTO> list = itemService.listByDate(eventDate);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).itemName()).isEqualTo("Suco de Acerola");
        assertThat(list.get(0).collaboratorName()).isEqualTo("Fulano de Tal");
        CoffeeItemDTO marked = itemService.mark(item.id(), true);
        assertThat(marked.brought()).isTrue();
    }
}