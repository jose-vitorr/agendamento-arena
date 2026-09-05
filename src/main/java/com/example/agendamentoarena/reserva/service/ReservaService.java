package com.example.agendamentoarena.reserva.service;

import com.example.agendamentoarena.horario.Horario;
import com.example.agendamentoarena.horario.HorarioRepository;
import com.example.agendamentoarena.quadra.Modalidade;
import com.example.agendamentoarena.quadra.ModalidadeRepository;
import com.example.agendamentoarena.quadra.QuadraModalidade;
import com.example.agendamentoarena.quadra.QuadraModalidadeRepository;
import com.example.agendamentoarena.reserva.Reserva;
import com.example.agendamentoarena.reserva.ReservaRepository;
import com.example.agendamentoarena.reserva.dto.ReservaRequestDTO;
import com.example.agendamentoarena.reserva.dto.ReservaResponseDTO;
import com.example.agendamentoarena.usuario.Usuario;
import com.example.agendamentoarena.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HorarioRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModalidadeRepository modalidadeRepository;
    private final QuadraModalidadeRepository quadraModalidadeRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          HorarioRepository horarioRepository,
                          UsuarioRepository usuarioRepository,
                          ModalidadeRepository modalidadeRepository,
                          QuadraModalidadeRepository quadraModalidadeRepository) {
        this.reservaRepository = reservaRepository;
        this.horarioRepository = horarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.modalidadeRepository = modalidadeRepository;
        this.quadraModalidadeRepository = quadraModalidadeRepository;
    }

    @Transactional
    public ReservaResponseDTO criar(Long usuarioId, ReservaRequestDTO dto) {

        // Busca e valida o usuário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Busca e valida o horário
        Horario horario = horarioRepository.findById(dto.horarioId())
                .orElseThrow(() -> new IllegalArgumentException("Horário não encontrado"));

        // RN04 — só reserva horário disponível
        if (!horario.getStatus().equals("disponivel")) {
            throw new IllegalStateException("Horário não está disponível para reserva");
        }

        // Busca e valida a modalidade
        Modalidade modalidade = modalidadeRepository.findById(dto.modalidadeId())
                .orElseThrow(() -> new IllegalArgumentException("Modalidade não encontrada"));

        // Busca o preço da combinação quadra + modalidade
        QuadraModalidade quadraModalidade = quadraModalidadeRepository
                .findByQuadraId(horario.getQuadra().getId())
                .stream()
                .filter(qm -> qm.getModalidade().getId().equals(dto.modalidadeId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Essa quadra não oferece a modalidade selecionada"));

        // Calcula valor total com base na duração do horário
        long minutos = java.time.Duration.between(
                horario.getHoraInicio(),
                horario.getHoraFim()
        ).toMinutes();

        BigDecimal horas = BigDecimal.valueOf(minutos)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        BigDecimal valorTotal = quadraModalidade.getValorHora()
                .multiply(horas)
                .setScale(2, RoundingMode.HALF_UP);

        // RN01/RN02 — calcula valor do sinal ou pagamento integral
        BigDecimal percentualSinal = BigDecimal.valueOf(
                horario.getQuadra().getArena().getPercentualReembolsoCancelamento()
                        .intValue() == 100 ? 50 : 30
        ).divide(BigDecimal.valueOf(100));

        BigDecimal valorPago = Boolean.TRUE.equals(dto.pagamentoIntegral())
                ? valorTotal
                : valorTotal.multiply(percentualSinal).setScale(2, RoundingMode.HALF_UP);

        // Muda status do horário pra bloqueado temporariamente (RN04/RF12)
        horario.setStatus("bloqueado_temporario");
        horario.setBloqueadoAte(LocalDateTime.now().plusMinutes(10));
        horarioRepository.save(horario);

        // Cria a reserva com status pendente (aguardando pagamento)
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setHorario(horario);
        reserva.setModalidade(modalidade);
        reserva.setValorTotal(valorTotal);
        reserva.setValorPago(valorPago);
        reserva.setStatus("pendente");
        reserva.setCriadoEm(LocalDateTime.now());
        reserva.setAtualizadoEm(LocalDateTime.now());

        Reserva salva = reservaRepository.save(reserva);
        return ReservaResponseDTO.from(salva);
    }

    // RF20 — histórico de reservas do cliente (UC08)
    public List<ReservaResponseDTO> listarPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ReservaResponseDTO::from)
                .toList();
    }

    // RN06 — cancelamento com regra de reembolso
    @Transactional
    public ReservaResponseDTO cancelar(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada"));

        if (reserva.getStatus().equals("cancelada")) {
            throw new IllegalStateException("Reserva já está cancelada");
        }

        // Libera o horário de volta pra disponível
        Horario horario = reserva.getHorario();
        horario.setStatus("disponivel");
        horario.setBloqueadoAte(null);
        horarioRepository.save(horario);

        reserva.setStatus("cancelada");
        reserva.setAtualizadoEm(LocalDateTime.now());

        Reserva salva = reservaRepository.save(reserva);
        return ReservaResponseDTO.from(salva);
    }

    // UC14 — admin confirma recebimento do saldo presencial (RF18)
    @Transactional
    public ReservaResponseDTO confirmarSaldoPresencial(Long reservaId, Long adminId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada"));

        if (!reserva.getStatus().equals("confirmada")) {
            throw new IllegalStateException("Reserva precisa estar confirmada para acertar saldo");
        }

        reserva.setSaldoConfirmadoEm(LocalDateTime.now());
        reserva.setAtualizadoEm(LocalDateTime.now());

        Reserva salva = reservaRepository.save(reserva);
        return ReservaResponseDTO.from(salva);
    }
}