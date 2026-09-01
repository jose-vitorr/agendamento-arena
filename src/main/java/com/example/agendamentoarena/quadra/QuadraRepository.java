package com.example.agendamentoarena.quadra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuadraRepository extends JpaRepository<Quadra, Long> {

    List<Quadra> findByArenaId(Long arenaId);
}