package com.example.agendamentoarena.reserva;

import com.example.agendamentoarena.administrador.Administrador;
import com.example.agendamentoarena.horario.Horario;
import com.example.agendamentoarena.quadra.Modalidade;
import com.example.agendamentoarena.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = false, unique = true)
    private Horario horario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modalidade_id", nullable = false)
    private Modalidade modalidade;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_pago", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPago;

    @Column(nullable = false, length = 20)
    private String status;

    // Campos preparados para features futuras (nullable — sem impacto no MVP)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorrencia_id")
    private Recorrencia recorrencia;

    @Column(name = "saldo_confirmado_em")
    private LocalDateTime saldoConfirmadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmado_por_admin_id")
    private Administrador confirmadoPorAdmin;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}