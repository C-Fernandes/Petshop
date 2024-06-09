package br.com.imd.petshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.imd.petshop.Entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

}
