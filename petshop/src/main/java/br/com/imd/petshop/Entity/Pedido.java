package br.com.imd.petshop.Entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "data")
    private Date data;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "funcionario_email")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "cliente_email")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido")
    private List<PedidoHasProduto> pedidoHasProdutoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<PedidoHasProduto> getPedidoHasProdutoList() {
        return pedidoHasProdutoList;
    }

    public void setPedidoHasProdutoList(List<PedidoHasProduto> pedidoHasProdutoList) {
        this.pedidoHasProdutoList = pedidoHasProdutoList;
    }
}
