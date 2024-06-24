package br.com.imd.petshop.Entity;

import java.util.Date;

public class Pet {

    private Long id;
    private String nome;
    private Date dataDeNascimento;
    private Integer idade;
    private Usuario dono;
    private String imagem;
    private Raca raca;
    private Boolean active;
    private Double peso;
    private char sexo;

    public String getImagem() {
        return this.imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

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

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Usuario getDono() {
        return this.dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public Raca getRaca() {
        return raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    public Boolean getActive() {
        return this.active;

    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getPeso() {
        return this.peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public char getSexo() {
        return this.sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
}
