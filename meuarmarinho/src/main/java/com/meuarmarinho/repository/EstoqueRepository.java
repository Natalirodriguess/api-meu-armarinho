package com.meuarmarinho.repository;

import com.meuarmarinho.model.EstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EstoqueRepository extends JpaRepository<EstoqueEntity, Long> {
    EstoqueEntity findByProdutoId(Long idProduto);

    @Transactional
    void deleteByProdutoId(Long id);
}
