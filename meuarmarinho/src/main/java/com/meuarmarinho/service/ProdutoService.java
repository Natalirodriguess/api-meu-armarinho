package com.meuarmarinho.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.meuarmarinho.dto.ProdutoRequest;
import com.meuarmarinho.dto.ProdutoResponse;
import com.meuarmarinho.model.EstoqueEntity;
import com.meuarmarinho.model.ProdutoEntity;
import com.meuarmarinho.repository.EstoqueRepository;
import com.meuarmarinho.repository.ProdutoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.meuarmarinho.helper.html.ConsultarCodigoBarraHtml.buscaProdutoPorCodigoBarra;


@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;


    public ProdutoService(ProdutoRepository produtoRepository, EstoqueRepository estoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public ProdutoResponse cadastrarProduto(ProdutoRequest produtoRequest) {
        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setNome(produtoRequest.getNome());
        produtoEntity.setCodigoDeBarras(produtoRequest.getCodigoDeBarras());
        produtoEntity.setDescricao(produtoRequest.getDescricao());

        ProdutoEntity produtoSalvo = produtoRepository.save(produtoEntity);

        EstoqueEntity estoqueEntity = new EstoqueEntity();
        estoqueEntity.setProduto(produtoSalvo);
        estoqueEntity.setQuantidade(produtoRequest.getQuantidade());

        estoqueRepository.save(estoqueEntity);

        return ProdutoResponse.converterParaProdutoResponse(produtoSalvo, estoqueEntity);
    }

    public ProdutoResponse cadastrarProdutoPorImagem(BufferedImage bufferedImage, Integer quantidade) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);   // Inicializa o decodificador ZXing
        ProdutoEntity produto= new ProdutoEntity();
        EstoqueEntity estoqueEntity = new EstoqueEntity();

        ProdutoEntity produtoRepo = produtoRepository.findByCodigoDeBarras(result.getText());

        if(produtoRepo!=null){
            throw new RuntimeException("Produto já cadastrado");

        } else {
            String codigoBarra = result.getText();
            produto.setNome(buscaProdutoPorCodigoBarra(codigoBarra));
            produto.setCodigoDeBarras(result.getText());
            produto.setDescricao(produto.getNome());

            ProdutoEntity produtoSalvo = produtoRepository.save(produto);

            if(produtoSalvo == null) { //adicionar validação de código de barra: requisitos para ser considerado como codigo de barra
                throw new RuntimeException("Código do produto não registrado no sistema geral de dados ");
            }

            estoqueEntity.setProduto(produtoSalvo);
            estoqueEntity.setQuantidade(quantidade);
        }

        return ProdutoResponse.converterParaProdutoResponse(produto, estoqueEntity);
    }

    public ProdutoResponse atualizarProduto(ProdutoRequest produtoRequest) {
        boolean produtoExiste = produtoRepository.existsByCodigoDeBarras(produtoRequest.getCodigoDeBarras());
        ProdutoEntity produtoSalvo;
        EstoqueEntity estoqueEntity;

        if(produtoExiste){
            ProdutoEntity produtoEncontrado = produtoRepository.findByCodigoDeBarras(produtoRequest.getCodigoDeBarras());

            produtoEncontrado.setNome(produtoRequest.getNome());
            produtoEncontrado.setDescricao(produtoRequest.getDescricao());
            produtoEncontrado.setObservacao(produtoRequest.getObservacao());

            produtoSalvo = produtoRepository.save(produtoEncontrado);

            estoqueEntity = estoqueRepository.findByProdutoId(produtoSalvo.getId());
            if(estoqueEntity == null){
                EstoqueEntity estoqueEncontrado = new EstoqueEntity();
                estoqueEncontrado.setProduto(produtoSalvo);
                estoqueEncontrado.setQuantidade(produtoRequest.getQuantidade());
                estoqueRepository.save(estoqueEncontrado);
            }

            estoqueEntity.setProduto(produtoSalvo);
            estoqueEntity.setQuantidade(produtoRequest.getQuantidade());
            estoqueRepository.save(estoqueEntity);
        }
        else{
            throw new RuntimeException("Produto não  com encontrado com o codigo de barra informado: ");
        }


        return ProdutoResponse.converterParaProdutoResponse(produtoSalvo, estoqueEntity);
    }


    public ProdutoResponse buscarProdutoPorId(Long id) {
            Optional<ProdutoEntity> produtoPorId = produtoRepository.findById(id);

            if(produtoPorId.isPresent()){
                EstoqueEntity estoque = estoqueRepository.findByProdutoId(produtoPorId.get().getId());
                if(estoque!=null){
                    return ProdutoResponse.converterParaProdutoComCodigoResponse(produtoPorId, estoque);
                } else {
                    return ProdutoResponse.converterParaProdutoComCodigoResponse(produtoPorId, null);
                }

            } else {
                throw new RuntimeException("Produto não  com encontrado: ");
        }
    }

    public List<ProdutoResponse> buscarProdutos() {
        List<ProdutoEntity> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(produto -> {
                    EstoqueEntity estoque = estoqueRepository.findByProdutoId(produto.getId());
                    return ProdutoResponse.converterForProdutoResponse(produto, estoque);
                })
                .collect(Collectors.toList());
    }

    public boolean deletarProdutoPorId(Long id) {
        Optional<ProdutoEntity> produtoPorId = produtoRepository.findById(id);
        if(produtoPorId.isPresent()){
            estoqueRepository.deleteByProdutoId(produtoPorId.get().getId());
            produtoRepository.delete(produtoPorId.get());
            return true;
        } else {
            throw new RuntimeException("Produto já cadastrado");

        }
    }


}
