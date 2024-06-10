package br.com.imd.petshop.Entity;

public class Cliente {

    private Usuario usuario;
    private Long qtdPontos;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getQtdPontos() {
        return this.qtdPontos;
    }

    public void setQtdPontos(Long qtdPontos) {
        this.qtdPontos = qtdPontos;
    }
}