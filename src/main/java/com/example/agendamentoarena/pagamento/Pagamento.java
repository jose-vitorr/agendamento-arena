package com.example.agendamentoarena.pagamento;

import com.example.agendamentoarena.reserva.Reserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "forma_pagamento", nullable = false, length = 20)
    private String formaPagamento;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "valor_reembolsado", precision = 10, scale = 2)
    private BigDecimal valorReembolsado;

    @Column(name = "gateway_transacao_id", length = 100)
    private String gatewayTransacaoId;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}