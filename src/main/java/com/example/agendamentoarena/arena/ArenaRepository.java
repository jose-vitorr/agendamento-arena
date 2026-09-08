package com.example.agendamentoarena.arena;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArenaRepository extends JpaRepository<Arena, Long> {
    
    //metodo: 1 buscar arena por nome
    Optional<Arena> findByNome(String nome);

    //Listar as arenas
    List <Arena> findByAtivaArenas(boolean ativa);

    //Busca por telefone
    List <Arena> findBytelefoneWhatsapp(String telefoneWhatsapp);
}