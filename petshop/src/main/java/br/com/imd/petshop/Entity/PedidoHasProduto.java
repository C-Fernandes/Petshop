package br.com.imd.petshop.Entity;

import jakarta.persistence.*;

@Entity
public class PedidoHasProduto {
    @Id
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Id
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "quantidade")
    private Integer quantidade;

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
