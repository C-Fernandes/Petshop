package br.com.imd.petshop.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Produto {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")

    private String nome;
    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "ativo")
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "preco_id", nullable = false)
    private Preco preco;

    @OneToMany(mappedBy = "produto")
    private List<PedidoHasProduto> pedidoHasProdutoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Preco getPreco() {
        return preco;
    }

    public void setPreco(Preco preco) {
        this.preco = preco;
    }

    public List<PedidoHasProduto> getPedidoHasProdutoList() {
        return pedidoHasProdutoList;
    }

    public void setPedidoHasProdutoList(List<PedidoHasProduto> pedidoHasProdutoList) {
        this.pedidoHasProdutoList = pedidoHasProdutoList;
    }
}
