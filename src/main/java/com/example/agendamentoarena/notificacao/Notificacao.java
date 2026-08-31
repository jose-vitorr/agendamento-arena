package com.example.agendamentoarena.notificacao;

import com.example.agendamentoarena.reserva.Reserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Column(nullable = false, length = 20)
    private String canal;

    @Column(name = "status_envio", nullable = false, length = 20)
    private String statusEnvio;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;
}