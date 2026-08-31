package com.example.agendamentoarena.administrador;

import com.example.agendamentoarena.arena.Arena;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "administrador_arena")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorArena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = false)
    private Administrador administrador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arena_id", nullable = false)
    private Arena arena;

    @Column(nullable = false, length = 20)
    private String papel;
}