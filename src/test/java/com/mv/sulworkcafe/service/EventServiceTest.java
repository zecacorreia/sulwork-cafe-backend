package com.mv.sulworkcafe.service;

import com.mv.sulworkcafe.dto.CoffeeEventDTO;
import com.mv.sulworkcafe.entity.CoffeeEvent;
import com.mv.sulworkcafe.exception.BusinessException;
import com.mv.sulworkcafe.repository.jpa.CoffeeEventRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    CoffeeEventRepository jpaRepo = mock(CoffeeEventRepository.class);
    EventService service = new EventService(jpaRepo);

    @Test
    void create_savesAndReturnsWithId() {
        var date = LocalDate.now().plusDays(3);
        when(jpaRepo.existsByEventDate(date)).thenReturn(false);
        when(jpaRepo.save(any())).thenAnswer(inv -> {
            CoffeeEvent e = inv.getArgument(0);
            e.setId(10L);
            return e;
        });

        var saved = service.create(new CoffeeEventDTO(date));
        assertThat(saved.getId()).isEqualTo(10L);
        assertThat(saved.getEventDate()).isEqualTo(date);
    }

    @Test
    void create_rejectsPastOrTodayDates() {
        var date = LocalDate.now();
        assertThatThrownBy(() -> service.create(new CoffeeEventDTO(date)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("maior que a data atual");
    }
}
