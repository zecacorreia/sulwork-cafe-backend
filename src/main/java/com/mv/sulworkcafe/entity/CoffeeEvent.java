package com.mv.sulworkcafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;


@Entity @Table(name = "coffee_event")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CoffeeEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_date", nullable = false, unique = true)
    private LocalDate eventDate;
    private OffsetDateTime createdAt;
    @PrePersist void pre() { if (createdAt == null) createdAt = OffsetDateTime.now(); }
}