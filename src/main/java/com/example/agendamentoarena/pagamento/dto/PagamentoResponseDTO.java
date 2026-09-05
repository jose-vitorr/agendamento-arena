package com.example.agendamentoarena.pagamento.dto;

import com.example.agendamentoarena.pagamento.Pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDTO(
        Long id,
        Long reservaId,
        BigDecimal valor,
        String formaPagamento,
        String status,
        BigDecimal valorReembolsado,
        String gatewayTransacaoId,
        LocalDateTime criadoEm
) {
    public static PagamentoResponseDTO from(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getReserva().getId(),
                pagamento.getValor(),
                pagamento.getFormaPagamento(),
                pagamento.getStatus(),
                pagamento.getValorReembolsado(),
                pagamento.getGatewayTransacaoId(),
                pagamento.getCriadoEm()
        );
    }
}