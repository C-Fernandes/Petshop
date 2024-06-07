package br.com.imd.petshop.Entity;

import jakarta.persistence.*;

@Entity
public class Funcionario {

    @Id
    @OneToOne
    @JoinColumn(name = "usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "qtd_pontos")
    private Integer qtd_pontos;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getQtd_pontos() {
        return qtd_pontos;
    }

    public void setQtd_pontos(Integer qtd_pontos) {
        this.qtd_pontos = qtd_pontos;
    }
}
