package com.meuarmarinho.dto;

import com.meuarmarinho.model.EstoqueEntity;
import com.meuarmarinho.model.ProdutoEntity;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class ProdutoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Integer quantidade;
    private String codigoDeBarra;

    public ProdutoResponse(Long id, String nome, String descricao, String codigoDeBarras, Integer quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.codigoDeBarra = codigoDeBarras;
        this.quantidade = quantidade;
    }
    public ProdutoResponse(){

    }


    public static ProdutoResponse converterParaProdutoComCodigoResponse(Optional<ProdutoEntity> produtoEntity, EstoqueEntity estoqueEntity) {
        ProdutoResponse produtoResponse = new ProdutoResponse();
        produtoResponse.setId(produtoEntity.get().getId());
        produtoResponse.setId(produtoEntity.get().getId());
        produtoResponse.setNome(produtoEntity.get().getNome());
        produtoResponse.setDescricao(produtoEntity.get().getDescricao());
        produtoResponse.setCodigoDeBarra(produtoEntity.get().getCodigoDeBarras());
        if(estoqueEntity != null){
            produtoResponse.setQuantidade(estoqueEntity.getQuantidade());
        } else{
            produtoResponse.setQuantidade(null);
        }

        return produtoResponse;
    }

    public static ProdutoResponse converterParaProdutoResponse(ProdutoEntity produtoEntity, EstoqueEntity estoqueEntity){
        ProdutoResponse produtoResponse = new ProdutoResponse();
        produtoResponse.setId(produtoEntity.getId());
        produtoResponse.setNome(produtoEntity.getNome());
        produtoResponse.setDescricao(produtoEntity.getDescricao());
        produtoResponse.setQuantidade(estoqueEntity.getQuantidade());
        produtoResponse.setCodigoDeBarra(produtoEntity.getCodigoDeBarras());

        return produtoResponse;
    }


    public static ProdutoResponse converterForProdutoResponse(ProdutoEntity produtoEntity, EstoqueEntity estoqueEntity) {
        ProdutoResponse produtoResponse = new ProdutoResponse();
        produtoResponse.setId(produtoEntity.getId());
        produtoResponse.setNome(produtoEntity.getNome());
        produtoResponse.setDescricao(produtoEntity.getDescricao());
        produtoResponse.setCodigoDeBarra(produtoEntity.getCodigoDeBarras());

        if (estoqueEntity != null) { // Verifica se o estoque foi encontrado
            produtoResponse.setQuantidade(estoqueEntity.getQuantidade());
        } else {
            produtoResponse.setQuantidade(0); // Define 0 caso não haja registro de estoque
        }

        return produtoResponse;
    }




}

