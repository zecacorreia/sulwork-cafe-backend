package com.mv.sulworkcafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity @Table(name = "coffee_item")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CoffeeItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "event_id")
    private CoffeeEvent event;

    @ManyToOne(optional = false) @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    @Column(name = "item_name", nullable = false, length = 120)
    private String itemName;

    private Boolean brought; // null = não marcado
    private OffsetDateTime checkedAt;
    private OffsetDateTime createdAt;


    @PrePersist void pre() { if (createdAt == null) createdAt = OffsetDateTime.now(); }
}