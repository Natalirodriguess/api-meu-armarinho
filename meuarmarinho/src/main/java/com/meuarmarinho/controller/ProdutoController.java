package com.meuarmarinho.controller;

import com.google.zxing.NotFoundException;
import com.meuarmarinho.dto.ProdutoImagemRequest;
import com.meuarmarinho.dto.ProdutoRequest;
import com.meuarmarinho.dto.ProdutoResponse;
import com.meuarmarinho.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping()
    public ResponseEntity<ProdutoResponse> cadastrarProduto(@RequestBody ProdutoRequest produtoRequest) {
        ProdutoResponse produtoResponse = produtoService.cadastrarProduto(produtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoResponse);
    }

    @PostMapping("/upload-imagem")
    public ResponseEntity<?> cadastrarProdutoPorImagem(@ModelAttribute ProdutoImagemRequest produtoImagemRequest) {
        try {
            MultipartFile multipartFile = produtoImagemRequest.getImagem();
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

            if(bufferedImage == null){
                return  ResponseEntity.badRequest().body("Arquivo inválido ou formato não suportado.");
            }

           ProdutoResponse produtoResponse =  produtoService.cadastrarProdutoPorImagem(bufferedImage, produtoImagemRequest.getQuantidade());

            return ResponseEntity.status(HttpStatus.CREATED).body(produtoResponse);

        } catch (IOException e) {
             return ResponseEntity.status(500).body("Erro ao ler a imagem: " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Código de barras não encontrado na imagem.");
        }

    }

    @PutMapping()
    public ResponseEntity<?> atualizarProduto(@RequestBody ProdutoRequest produtoRequest) {
        try{
            ProdutoResponse response = produtoService.atualizarProduto(produtoRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException  e) {
            return ResponseEntity.status(404).body("Produto não existe para o código de barra informado: " + produtoRequest.getCodigoDeBarras());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProdutoPorId(@PathVariable Long id){
        ProdutoResponse response;
        try{
            response = produtoService.buscarProdutoPorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException  e) {
            return ResponseEntity.status(404).body("Produto não encontrado");
        }

    }
    @GetMapping()
    public ResponseEntity<?> buscarTodosProdutos(){
        List<ProdutoResponse> response;
        try{
            response = produtoService.buscarProdutos();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException  e) {
            return ResponseEntity.status(400).body("OPERACAO INVALIDA");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProdutoPorId(@PathVariable Long id) {
        if(produtoService.deletarProdutoPorId(id)){
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
