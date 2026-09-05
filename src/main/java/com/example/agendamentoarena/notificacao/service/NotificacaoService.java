package com.example.agendamentoarena.notificacao.service;

import com.example.agendamentoarena.notificacao.Notificacao;
import com.example.agendamentoarena.notificacao.NotificacaoRepository;
import com.example.agendamentoarena.notificacao.dto.NotificacaoResponseDTO;
import com.example.agendamentoarena.reserva.Reserva;
import com.example.agendamentoarena.reserva.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final ReservaRepository reservaRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository,
                              ReservaRepository reservaRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.reservaRepository = reservaRepository;
    }

    // RF14/RF16 — registra e dispara confirmação após pagamento aprovado
    @Transactional
    public NotificacaoResponseDTO enviarConfirmacao(Long reservaId, String canal) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada"));

        Notificacao notificacao = new Notificacao();
        notificacao.setReserva(reserva);
        notificacao.setCanal(canal);
        notificacao.setStatusEnvio("pendente");

        // Aqui futuramente chamará a API do WhatsApp (RF16) ou
        // publicará um evento no RabbitMQ pra processamento assíncrono
        // Por enquanto registra como "enviado" direto (MVP simplificado)
        notificacao.setStatusEnvio("enviado");
        notificacao.setDataEnvio(LocalDateTime.now());

        Notificacao salva = notificacaoRepository.save(notificacao);
        return NotificacaoResponseDTO.from(salva);
    }
}