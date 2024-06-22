package br.com.imd.petshop.DTO;

import org.springframework.web.multipart.MultipartFile;

import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;

public class ProdutoPrecoDTO {
    private Produto produto;
    private Preco preco;
    private MultipartFile imagem;

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Preco getPreco() {
        return this.preco;
    }

    public void setPreco(Preco preco) {
        this.preco = preco;
    }

    public MultipartFile getImagem() {
        return this.imagem;
    }

    public void setImagem(MultipartFile imagem) {
        this.imagem = imagem;
    }

}