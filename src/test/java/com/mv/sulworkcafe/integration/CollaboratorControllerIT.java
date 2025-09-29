package com.mv.sulworkcafe.integration;

import com.mv.sulworkcafe.controller.CollaboratorController;
import com.mv.sulworkcafe.dto.CollaboratorDTO;
import com.mv.sulworkcafe.entity.Collaborator;
import com.mv.sulworkcafe.repository.nativequery.CoffeeEventNativeRepositoryImpl;
import com.mv.sulworkcafe.repository.nativequery.CoffeeItemNativeRepositoryImpl;
import com.mv.sulworkcafe.repository.nativequery.CollaboratorNativeRepositoryImpl;
import com.mv.sulworkcafe.service.CollaboratorService;
import com.mv.sulworkcafe.service.EventService;
import com.mv.sulworkcafe.service.ItemService;
import com.mv.sulworkcafe.service.ParticipantFacadeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CollaboratorController.class)
@AutoConfigureMockMvc(addFilters = false)
class CollaboratorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean private CollaboratorService collaboratorService;
    @MockitoBean private EventService eventService;
    @MockitoBean private ItemService itemService;
    @MockitoBean private ParticipantFacadeService participantFacadeService;

    @MockitoBean private CoffeeItemNativeRepositoryImpl coffeeItemNativeRepositoryImpl;
    @MockitoBean private CoffeeEventNativeRepositoryImpl coffeeEventNativeRepositoryImpl;
    @MockitoBean private CollaboratorNativeRepositoryImpl collaboratorNativeRepositoryImpl;

    @Test
    void createCollaborator_returns201() throws Exception {
        when(collaboratorService.create(any(CollaboratorDTO.class)))
                .thenReturn(Collaborator.builder()
                        .id(1L)
                        .name("Fulano")
                        .cpf("06875931070")
                        .build());

        mvc.perform(post("/api/collaborators")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Fulano","cpf":"06875931070"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/collaborators/1")))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Fulano"))
                .andExpect(jsonPath("$.cpf").value("06875931070"))
                .andExpect(jsonPath("$.id").doesNotExist());
    }
}
