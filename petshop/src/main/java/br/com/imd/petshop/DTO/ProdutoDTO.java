package br.com.imd.petshop.DTO;

import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;

public class ProdutoDTO {
    private Produto produto;
    private Preco preco;

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

}