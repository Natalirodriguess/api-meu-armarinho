package com.meuarmarinho.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "t_estoque")
public class EstoqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;

    @OneToOne
    @JoinColumn(name = "fk_id_produto", nullable = false)
    private ProdutoEntity produto;

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
