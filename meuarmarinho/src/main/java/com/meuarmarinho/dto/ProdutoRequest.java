package com.meuarmarinho.dto;

import lombok.Data;

@Data
public class ProdutoRequest {

    private String codigoDeBarras;
    private String nome;
    private String descricao;
    private Integer quantidade;
    private String observacao;


    public ProdutoRequest(String codigoDeBarras, String nome, String descricao, Integer quantidade,String observacao) {
        this.codigoDeBarras = codigoDeBarras;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }
}
