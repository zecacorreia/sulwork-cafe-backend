package com.mv.sulworkcafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;


@Entity @Table(name = "collaborator")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Collaborator {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, length = 11, unique = true)
    private String cpf; // somente dígitos
    private OffsetDateTime createdAt;
    @PrePersist void pre() { if (createdAt == null) createdAt = OffsetDateTime.now(); }
}