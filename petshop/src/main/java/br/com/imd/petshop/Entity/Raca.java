package br.com.imd.petshop.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Raca {

    @Id
    @Column(name = "raca", nullable = false, unique = true)
    private String raca;

    @Column(name = "especie")
    private String especie;

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }
}
