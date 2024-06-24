package br.com.imd.petshop.DTO;

import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Entity.Raca;

public class PetDTO {
    private Pet pet;
    private Raca raca;
    private String email;

    public Pet getPet() {
        return this.pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Raca getRaca() {
        return this.raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

}
