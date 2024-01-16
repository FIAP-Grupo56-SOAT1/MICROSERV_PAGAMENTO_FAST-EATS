package br.com.fiap.fasteats.dataprovider.repository;

import br.com.fiap.fasteats.dataprovider.repository.entity.StatusPagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusPagamentoRepository extends JpaRepository<StatusPagamentoEntity, Long> {
    

    @Query("select s from StatusPagamento s where s.nome = upper(:nome)")
    List<StatusPagamentoEntity> findByNome (@Param("nome") String nome);
}
