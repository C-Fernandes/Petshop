package br.com.imd.petshop.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Repository.PetRepository;

@Service
public class PetService {

    private PetRepository petRepository;

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();

    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public Pet updatePet(Long id, Pet petDetails) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet não encontrado!"));

        pet.setNome(petDetails.getNome());
        pet.setId(petDetails.getId());
        pet.setIdade(petDetails.getIdade());
        pet.setNome(petDetails.getNome());
        pet.setRaca(petDetails.getRaca());
        pet.setUsuario(petDetails.getUsuario()); // Vai ser possivel mudar usuário?

        return petRepository.save(pet);
    }

    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet não encontrado!"));
        petRepository.delete(pet);

    }

}
