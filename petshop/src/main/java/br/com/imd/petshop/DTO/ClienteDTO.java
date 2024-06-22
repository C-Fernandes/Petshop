package br.com.imd.petshop.DTO;

import br.com.imd.petshop.Entity.Cep;

import java.util.Date;

public class ClienteDTO {
    private String email;
    private String senha;
    private String nome;
    private Date dataNascimento;
    private Integer idade;
    private String telefone;
    private String logradouro;
    private Long numero;
    private String bairro;
    private Cep cep;
    private Long qtdPontos;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    public Long getQtdPontos() {
        return qtdPontos;
    }

    public void setQtdPontos(Long qtdPontos) {
        this.qtdPontos = qtdPontos;
    }
}
