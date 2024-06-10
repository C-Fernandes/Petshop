package br.com.imd.petshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Service.PetService;

public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public Pet createPet(@RequestBody Pet pet) {
        return petService.createPet(pet);
    }

    @GetMapping
    public List<Pet> getAllPets() {
        return petService.getAllPets();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petService.getPetById(id).orElseThrow(() -> new RuntimeException("Pet não encontrado"));
        return ResponseEntity.ok().body(pet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petDetails) {
        Pet updatedPet = petService.updatePet(id, petDetails);
        return ResponseEntity.ok(updatedPet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
