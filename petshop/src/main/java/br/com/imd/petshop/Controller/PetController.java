package br.com.imd.petshop.Controller;

import br.com.imd.petshop.DTO.PetDTO;
import br.com.imd.petshop.Entity.Cliente;
import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Entity.Raca;
import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Entity.UsuarioLogado;
import br.com.imd.petshop.Service.PetService;
import br.com.imd.petshop.Service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pets")
public class PetController {
    private static final String UPLOAD_DIR = "src/main/resources/static/images/uploads/pets/";
    private final PetService petService;
    private final UsuarioLogado usuarioLogado;

    private final UsuarioService usuarioService;

    @Autowired
    public PetController(PetService petService, UsuarioLogado usuarioLogado, UsuarioService usuarioService) {
        this.petService = petService;
        this.usuarioLogado = usuarioLogado;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String cadastro(Model model) {
        try {

            Usuario usuario = usuarioService.findUsuario(usuarioLogado.getEmail());
            List<Pet> pets = petService.findAll(usuarioLogado.getEmail());
            model.addAttribute("pets", pets);
            model.addAttribute("usuario", usuario.getNome());
            return "pets";
        } catch (Exception e) {
            if (usuarioLogado.getEmail() == null) {
                return "tela-login";

            }
            return null;
        }
    }

    @GetMapping("/listar")
    @ResponseBody
    public ResponseEntity<List<Pet>> listarPets() {
        try {
            System.out.println(usuarioLogado.getEmail());
            List<Pet> pets = petService.findAll(usuarioLogado.getEmail());
            return ResponseEntity.ok(pets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cadastro-pet")
    public ResponseEntity<?> cadastrarPet(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("petDTO") String petJson) {

        try {
            // Convertendo o JSON recebido em objeto PetDTO
            ObjectMapper objectMapper = new ObjectMapper();

            PetDTO petDTO = objectMapper.readValue(petJson, PetDTO.class);

            // Obtendo o objeto Pet do PetDTO
            Pet pet = petDTO.getPet();
            Usuario u = usuarioService.findUsuario(usuarioLogado.getEmail());
            System.out.println("datanascimento:" + petDTO.getPet().getDataDeNascimento());
            Cliente usuario = new Cliente();
            usuario.setEmail(u.getEmail());
            String urlImagem = "padrao.jpg"; // Nome da imagem padrão

            if (file != null && !file.isEmpty()) {
                // Salva a nova imagem
                urlImagem = petService.salvarNovaImagem(file, UPLOAD_DIR);
            }

            pet.setImagem(urlImagem);
            pet.setDono(usuario);

            Raca raca = petDTO.getRaca();
            pet.setRaca(raca);
            petService.inserirPet(pet);

            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Pet cadastrado com sucesso!");
            response.put("imagem", urlImagem);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("errors", Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Map<String, Object>> atualizarPet(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("petDTO") String petJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PetDTO petDTO = objectMapper.readValue(petJson, PetDTO.class);
            Pet pet = petDTO.getPet();
            pet.setRaca(petDTO.getRaca());
            ResponseEntity<Map<String, Object>> validationResponse = petService
                    .validarPetAtualizacao(pet);

            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.badRequest().body(validationResponse.getBody());
            }

            String urlImagem = "padrao.jpeg";
            // Se uma nova imagem foi enviada, processa e salva como no cadastro
            if (file != null && !file.isEmpty()) {
                // Salva a nova imagem
                urlImagem = petService.salvarNovaImagem(file, UPLOAD_DIR);
                System.out.println(urlImagem);
                petService.removerImagem(petService.findById(pet.getId()).getImagem(), UPLOAD_DIR);

            } else {
                // Se nenhum novo arquivo de imagem foi enviado, mantém a imagem existente
                Pet petExistente = petService.findById(pet.getId());
                urlImagem = petExistente.getImagem();
            }
            pet.setImagem(urlImagem);

            // Atualiza o pet
            petService.atualizarPet(pet);

            // Prepara a resposta com a URL da imagem atualizada, se disponível
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Pet atualizado com sucesso!");
            response.put("imagem", urlImagem); // Adiciona a URL da imagem ao objeto de resposta
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("errors", Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<Pet>> buscarPets(@RequestParam("nome") String nome) {

        try {
            List<Pet> pets = petService.buscarPorNome(nome, usuarioLogado.getEmail());
            return ResponseEntity.ok(pets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deletarPet(@PathVariable Long id) {
        try {
            Pet pet = petService.findById(id);
            petService.deletarPet(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content em caso de sucesso
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao deletar o pet");
        }
    }

}
