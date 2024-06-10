package br.com.imd.petshop.Controller;

import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/pets")
    public String listarPets(Model model) {
        List<Pet> pets = petService.listarPets();
        model.addAttribute("pets", pets);
        return "pets";
    }
}
