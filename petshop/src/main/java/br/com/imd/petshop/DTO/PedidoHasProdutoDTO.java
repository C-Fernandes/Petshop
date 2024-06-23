package br.com.imd.petshop.DTO;

import java.util.Date;
import java.util.List;

public class PedidoHasProdutoDTO {

    private Long pedido_id;

    private Date data;

    private double valor;

    private List<ProdutoListDTO> produtos;

    public List<ProdutoListDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoListDTO> produtos) {
        this.produtos = produtos;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(Long pedido_id) {
        this.pedido_id = pedido_id;
    }
}
