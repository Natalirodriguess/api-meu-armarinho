package com.meuarmarinho.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@Table(name = "t_produto")
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigoDeBarras;

    private String nome;
    private String descricao;
    private String observacao;
    private String linkImagem;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    private UUID version;

    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
        version = UUID.randomUUID();
    }
}
