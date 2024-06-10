package br.com.imd.petshop.Entity;

public class Servico {

    private Long id;

    private String nome;

    private String descricao;

    private Integer pontosServico;

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
