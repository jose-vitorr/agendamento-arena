package com.example.agendamentoarena.arena;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "arena")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Arena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 255)
    private String endereco;

    @Column(name = "link_maps", length = 500)
    private String linkMaps;

    @Column(name = "telefone_whatsapp", nullable = false, length = 20)
    private String telefoneWhatsapp;

    @Column(name = "prazo_minimo_cancelamento_horas", nullable = false)
    @Builder.Default
    private Integer prazoMinimoCancelamentoHoras = 24;

    @Column(name = "percentual_reembolso_cancelamento", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal percentualReembolsoCancelamento = new BigDecimal("100.00");

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}