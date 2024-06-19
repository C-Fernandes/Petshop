package br.com.imd.petshop.DTO;

import br.com.imd.petshop.Entity.Produto;

public class ProdutoPrecoDTO {
    private String nome;
    private int quantidade;
    private Boolean ativo;

    private double preco;

    public Produto getProduto() {
        return new Produto(nome, quantidade, ativo);
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Boolean isAtivo() {
        return this.ativo;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

}
