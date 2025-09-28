package com.mv.sulworkcafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity @Table(name = "collaborator")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Collaborator {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @OneToMany(mappedBy = "collaborator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoffeeItem> items;

    private OffsetDateTime createdAt;

    @PrePersist void pre() { if (createdAt == null) createdAt = OffsetDateTime.now(); }
}