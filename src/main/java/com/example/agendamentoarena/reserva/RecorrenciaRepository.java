package com.example.agendamentoarena.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecorrenciaRepository extends JpaRepository<Recorrencia, Long> {
}