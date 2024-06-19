package br.com.imd.petshop.Entity;

public class Raca {

    private String raca;

    private String especie;

    public Raca() {
    }

    public Raca(String raca, String especie) {
        this.raca = raca;
        this.especie = especie;
    }

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
