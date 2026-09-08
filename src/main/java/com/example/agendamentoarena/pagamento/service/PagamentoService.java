package com.example.agendamentoarena.pagamento.service;

import com.example.agendamentoarena.horario.Horario;
import com.example.agendamentoarena.horario.HorarioRepository;
import com.example.agendamentoarena.pagamento.Pagamento;
import com.example.agendamentoarena.pagamento.PagamentoRepository;
import com.example.agendamentoarena.pagamento.dto.PagamentoResponseDTO;
import com.example.agendamentoarena.reserva.Reserva;
import com.example.agendamentoarena.reserva.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ReservaRepository reservaRepository;
    private final HorarioRepository horarioRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository,
                            ReservaRepository reservaRepository,
                            HorarioRepository horarioRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.reservaRepository = reservaRepository;
        this.horarioRepository = horarioRepository;
    }

    // Chamado pelo webhook do gateway quando pagamento é aprovado (RF11)
    @Transactional
    public PagamentoResponseDTO confirmarPagamento(String gatewayTransacaoId) {

        Pagamento pagamento = pagamentoRepository
                .findByGatewayTransacaoId(gatewayTransacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado"));

        if (pagamento.getStatus().equals("aprovado")) {
            throw new IllegalStateException("Pagamento já foi confirmado");
        }

        // Confirma o pagamento
        pagamento.setStatus("aprovado");
        pagamento.setAtualizadoEm(LocalDateTime.now());
        pagamentoRepository.save(pagamento);

        // Confirma a reserva e marca o horário como reservado (RN04)
        Reserva reserva = pagamento.getReserva();
        reserva.setStatus("confirmada");
        reserva.setAtualizadoEm(LocalDateTime.now());
        reservaRepository.save(reserva);

        Horario horario = reserva.getHorario();
        horario.setStatus("reservado");
        horario.setBloqueadoAte(null);
        horarioRepository.save(horario);

        return PagamentoResponseDTO.from(pagamento);
    }

    // RN14 — reembolso quando arena cancela (RF19)
    @Transactional
    public PagamentoResponseDTO reembolsar(Long pagamentoId, BigDecimal valorReembolsado) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado"));

        pagamento.setStatus("reembolsado");
        pagamento.setValorReembolsado(valorReembolsado);
        pagamento.setAtualizadoEm(LocalDateTime.now());

        Pagamento salvo = pagamentoRepository.save(pagamento);
        return PagamentoResponseDTO.from(salvo);
    }
}