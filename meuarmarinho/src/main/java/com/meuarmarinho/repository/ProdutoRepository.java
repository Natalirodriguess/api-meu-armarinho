package com.meuarmarinho.repository;

import com.meuarmarinho.model.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    ProdutoEntity findByCodigoDeBarras(String text);
    boolean existsByCodigoDeBarras(String codigoDeBarras);
    ProdutoEntity findProdutoById(Integer id);

    Optional<ProdutoEntity> save( Optional<ProdutoEntity> produto);
    ProdutoEntity findProdutoByCodigoDeBarras(String text);

}
