package br.com.imd.petshop.Entity;

public class PedidoHasProduto {
    private Pedido pedido;

    private Produto produto;

    private Integer quantidade;

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
