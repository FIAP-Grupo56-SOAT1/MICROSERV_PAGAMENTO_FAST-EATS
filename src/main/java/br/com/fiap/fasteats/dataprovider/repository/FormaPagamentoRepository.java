package br.com.fiap.fasteats.dataprovider.repository;

import br.com.fiap.fasteats.dataprovider.repository.entity.FormaPagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamentoEntity, Long> {
    @Query("select s from FormaPagamento s where upper(s.nome) = upper(:nome)")
    List<FormaPagamentoEntity> findByNome (@Param("nome") String nome);
}