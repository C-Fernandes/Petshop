package br.com.imd.petshop.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

@Entity
public class Servico {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "pontos_servico")
    private Integer pontosServico;

    @ManyToOne
    @JoinColumn(name = "preco_id", nullable = false)
    private Preco preco;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getPontosServico() {
        return pontosServico;
    }

    public void setPontosServico(Integer pontosServico) {
        this.pontosServico = pontosServico;
    }

    public Preco getPreco() {
        return preco;
    }

    public void setPreco(Preco preco) {
        this.preco = preco;
    }
}
