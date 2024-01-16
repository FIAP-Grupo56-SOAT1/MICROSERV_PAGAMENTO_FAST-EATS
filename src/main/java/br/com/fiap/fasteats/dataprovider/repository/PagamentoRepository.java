package br.com.fiap.fasteats.dataprovider.repository;

import br.com.fiap.fasteats.dataprovider.repository.entity.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Long> {
    Optional<PagamentoEntity> findFirstByPedidoIdOrderByDataHoraCriadoDesc(long pedidoId);

    @Query("""
            select pg from Pagamento pg
            where pg.idPagamentoExterno = :idPagamentoExterno
            """)
    Optional<PagamentoEntity> findPagamentoByIdPagamentoExterno(long idPagamentoExterno);
}
