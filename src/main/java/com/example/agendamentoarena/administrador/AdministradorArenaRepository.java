package com.example.agendamentoarena.administrador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorArenaRepository extends JpaRepository<AdministradorArena, Long> {

    List<AdministradorArena> findByAdministradorId(Long administradorId);
}