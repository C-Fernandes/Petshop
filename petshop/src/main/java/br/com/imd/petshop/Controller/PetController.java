package br.com.imd.petshop.Controller;

import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Service.PetService;
import br.com.imd.petshop.Service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listarPets")
    public String listarPets(Model model) {
        List<Pet> pets = petService.listarPets();
        model.addAttribute("pets", pets);
        return "pets";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastrar-pet";
    }

    @PostMapping("/cadastro-pet")
    public ResponseEntity<String> cadastrarPet(@RequestParam("usuarioEmail") String usuarioEmail,
            @RequestBody Pet pet) {
        Usuario usuario = usuarioService.findUsuario(usuarioEmail);
        if (usuario != null) {
            pet.setDono(usuario);
            petService.inserirPet(pet);
            return ResponseEntity.ok("Pet cadastrado com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }
    }

}
