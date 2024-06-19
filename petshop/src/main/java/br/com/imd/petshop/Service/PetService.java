package br.com.imd.petshop.Service;

import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Repository.PetRepository;
import br.com.imd.petshop.Repository.RacaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private RacaRepository racaRepository;

    public List<Pet> listarPets() {
        return petRepository.listarPets();
    }

    public Pet procurarPorId(Long id) {
        return petRepository.procurarPorId(id);
    }

    public List<Pet> procurarPorNome(String nome) {
        return petRepository.procurarPorNome(nome);
    }

    public void inserirPet(Pet pet) {
        String especie = pet.getRaca().getEspecie();
        if (racaRepository.findByEspecie(especie) != null) {
            racaRepository.save(pet.getRaca());
        }
        petRepository.inserirPet(pet);
    }

    public void deletarPet(Long id) {
        petRepository.deletar(id);
    }

    public List<Pet> findByUser(String emailUser) {
        return petRepository.findByUser(emailUser);

    }
}