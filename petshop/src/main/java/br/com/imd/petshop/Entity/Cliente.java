package br.com.imd.petshop.Entity;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @OneToOne
    @JoinColumn(name = "usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "cargo")
    private String cargo;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
