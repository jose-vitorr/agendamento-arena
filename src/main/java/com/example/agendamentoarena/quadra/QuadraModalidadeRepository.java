package com.example.agendamentoarena.quadra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuadraModalidadeRepository extends JpaRepository<QuadraModalidade, Long> {

    List<QuadraModalidade> findByQuadraId(Long quadraId);
}