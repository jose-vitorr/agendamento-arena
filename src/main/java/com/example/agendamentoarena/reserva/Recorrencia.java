package com.example.agendamentoarena.reserva;

import com.example.agendamentoarena.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "recorrencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "dia_semana", nullable = false)
    private Short diaSemana;

    @Column(name = "horario_referencia", nullable = false)
    private LocalTime horarioReferencia;

    @Column(nullable = false)
    private Boolean ativa;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
}