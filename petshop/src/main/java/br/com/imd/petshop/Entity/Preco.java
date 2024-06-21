package br.com.imd.petshop.Entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Preco {

    private Long id;
    private Double valor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")

    private Date data;

    public Preco(double preco) {
        this.valor = preco;

    }

    public Preco(Long id, Double valor, Date data) {
        this.id = id;
        this.valor = valor;
        this.data = data;
    }

    public Preco(Long id, Double valor) {
        this.id = id;
        this.valor = valor;
    }

    public Preco() {
    }

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

}
