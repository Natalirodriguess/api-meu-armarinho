package com.meuarmarinho.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProdutoImagemRequest {

    private MultipartFile imagem;
    private Integer quantidade;

    public ProdutoImagemRequest(MultipartFile imagem, Integer quantidade) {
        this.imagem = imagem;
        this.quantidade = quantidade;
    }

}
