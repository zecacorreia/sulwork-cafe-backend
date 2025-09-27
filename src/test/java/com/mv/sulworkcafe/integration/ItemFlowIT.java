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
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("sulwork_cafe_it")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.flyway.enabled", () -> "true");      // roda V1__init.sql
        r.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    @Autowired CollaboratorService collaboratorService;
    @Autowired EventService eventService;
    @Autowired ItemService itemService;

    @Test
    @DisplayName("Fluxo completo: colaborador → evento → item → listar → marcar")
    void endToEndFlow() {
        var collab = collaboratorService.create(new CollaboratorDTO("Fulano de Tal", "12478617109"));
        assertThat(collab.getId()).isNotNull();

        LocalDate eventDate = LocalDate.now().plusDays(5);
        var event = eventService.create(new CoffeeEventDTO(eventDate));
        assertThat(event.getId()).isNotNull();

        CoffeeItemDTO item = itemService.create(new CoffeeItemCreateDTO(eventDate, "12478617109", "Suco de Acerola"));
        assertThat(item.id()).isNotNull();
        assertThat(item.brought()).isNull();

        List<CoffeeItemDTO> list = itemService.listByDate(eventDate);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).itemName()).isEqualTo("Suco de Acerola");
        assertThat(list.get(0).collaboratorName()).isEqualTo("Fulano de Tal");

        CoffeeItemDTO marked = itemService.mark(item.id(), true);
        assertThat(marked.brought()).isTrue();
    }
}
