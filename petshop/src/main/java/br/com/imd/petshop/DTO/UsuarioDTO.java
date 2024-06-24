package br.com.imd.petshop.DTO;

import java.util.Date;

import br.com.imd.petshop.Entity.Cep;

public class UsuarioDTO {
    private String email;
    private String senha;
    private String nome;
    private Date dataDeNascimento;
    private Integer idade;
    private String telefone;
    private String logradouro;
    private Long numero;
    private String bairro;
    private Cep cep;
    private Long qtdPontos;
    private String cargo;
    private Boolean active;

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

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }


}
