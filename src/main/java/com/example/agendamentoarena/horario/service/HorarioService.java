package com.example.agendamentoarena.horario.service;

import com.example.agendamentoarena.horario.Horario;
import com.example.agendamentoarena.horario.HorarioRepository;
import com.example.agendamentoarena.horario.dto.HorarioRequestDTO;
import com.example.agendamentoarena.horario.dto.HorarioResponseDTO;
import com.example.agendamentoarena.quadra.Quadra;
import com.example.agendamentoarena.quadra.QuadraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final QuadraRepository quadraRepository;

    public HorarioService(HorarioRepository horarioRepository,
                          QuadraRepository quadraRepository) {
        this.horarioRepository = horarioRepository;
        this.quadraRepository = quadraRepository;
    }

    // RF08 — consultar horários disponíveis de uma quadra numa data
    public List<HorarioResponseDTO> listarDisponiveis(Long quadraId, LocalDate data) {
        return horarioRepository
                .findByQuadraIdAndData(quadraId, data)
                .stream()
                .filter(h -> h.getStatus().equals("disponivel"))
                .map(HorarioResponseDTO::from)
                .toList();
    }

    // Admin cria horários na agenda da quadra (RF17)
    @Transactional
    public HorarioResponseDTO criar(HorarioRequestDTO dto) {
        Quadra quadra = quadraRepository.findById(dto.quadraId())
                .orElseThrow(() -> new IllegalArgumentException("Quadra não encontrada"));

        // RN: hora fim deve ser depois de hora início
        if (!dto.horaFim().isAfter(dto.horaInicio())) {
            throw new IllegalArgumentException("Hora de fim deve ser depois da hora de início");
        }

        Horario horario = new Horario();
        horario.setQuadra(quadra);
        horario.setData(dto.data());
        horario.setHoraInicio(dto.horaInicio());
        horario.setHoraFim(dto.horaFim());
        horario.setStatus("disponivel");
        horario.setCriadoEm(LocalDateTime.now());

        Horario salvo = horarioRepository.save(horario);
        return HorarioResponseDTO.from(salvo);
    }
}