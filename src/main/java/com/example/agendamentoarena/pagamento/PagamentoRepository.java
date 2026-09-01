package com.example.agendamentoarena.pagamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByReservaId(Long reservaId);

    Optional<Pagamento> findByGatewayTransacaoId(String gatewayTransacaoId);
}