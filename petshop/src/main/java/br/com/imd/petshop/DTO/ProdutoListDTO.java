package br.com.imd.petshop.DTO;

public class ProdutoListDTO {

    private Long produto_id;

    private Long pedido_id;

    private String produto_nome;

    private double produto_preco;

    private int quantidade;

    public Long getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(Long produto_id) {
        this.produto_id = produto_id;
    }

    public Long getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(Long pedido_id) {
        this.pedido_id = pedido_id;
    }

    public String getProduto_nome() {
        return produto_nome;
    }

    public void setProduto_nome(String produto_nome) {
        this.produto_nome = produto_nome;
    }

    public double getProduto_preco() {
        return produto_preco;
    }

    public void setProduto_preco(double produto_preco) {
        this.produto_preco = produto_preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
