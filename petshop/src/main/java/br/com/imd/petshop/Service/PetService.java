package br.com.imd.petshop.Service;

import br.com.imd.petshop.DTO.PetDTO;
import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Entity.Raca;
import br.com.imd.petshop.Repository.PetRepository;
import br.com.imd.petshop.Repository.RacaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private RacaRepository racaRepository;

    public Pet findById(Long id) {
        return petRepository.procurarPorId(id);
    }

    public List<Pet> procurarPorNome(String nome) {
        return petRepository.procurarPorNome(nome);
    }

    public void inserirPet(Pet pet) {
        System.out.println("raca:" + racaRepository.findByRaca(pet.getRaca().getRaca()));
        if (racaRepository.findByRaca(pet.getRaca().getRaca()) == null) {
            racaRepository.save(pet.getRaca());
        }

        System.out.println(pet.getDataDeNascimento());
        petRepository.inserirPet(pet);
    }

    public void deletarPet(Long id) {
        petRepository.desativarPet(id);
    }

    public List<Pet> findAll(String emailUser) {

        List<Pet> pets = petRepository.listarPets(emailUser);
        for (Pet pet : pets) {
            calcularIdadeEmMeses(pet); // Calcula a idade em meses e seta o atributo idade
            pet.setRaca(racaRepository.buscarPorRaca(pet.getRaca().getRaca()));
        }
        return pets;

    }

    private void calcularIdadeEmMeses(Pet pet) {
        Date dataNascimento = pet.getDataDeNascimento(); // Assume que getDataDeNascimento retorna um objeto Date
        Date hoje = new Date(); // Data atual

        Calendar calNascimento = Calendar.getInstance();
        calNascimento.setTime(dataNascimento);
        int anoNascimento = calNascimento.get(Calendar.YEAR);
        int mesNascimento = calNascimento.get(Calendar.MONTH);

        Calendar calHoje = Calendar.getInstance();
        calHoje.setTime(hoje);
        int anoAtual = calHoje.get(Calendar.YEAR);
        int mesAtual = calHoje.get(Calendar.MONTH);

        int idadeMeses = (anoAtual - anoNascimento) * 12 + (mesAtual - mesNascimento);
        pet.setIdade(idadeMeses);
    }

    public void removerImagem(String nomeImagem, String UPLOAD_DIR) {
        System.out.println("Nome imagem : " + nomeImagem);
        if (!nomeImagem.equals("padrao.jpg")) {
            Path pathImagemAntiga = Paths.get(UPLOAD_DIR + nomeImagem);
            try {
                Files.deleteIfExists(pathImagemAntiga);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void atualizarPet(Pet pet) {
        petRepository.atualizarPet(pet);
    }

    public String salvarNovaImagem(MultipartFile file, String UPLOAD_DIR) throws IOException {
        String nomeImagem = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        byte[] bytesImagem = file.getBytes();
        Path path = Paths.get(UPLOAD_DIR + nomeImagem);
        Files.write(path, bytesImagem);

        // Retorna apenas o caminho relativo da imagem, sem o prefixo do domínio
        return nomeImagem;
    }

    public ResponseEntity<Map<String, Object>> validarPetAtualizacao(Pet pet) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = new ArrayList<>();

        // Validando campos obrigatórios
        if (pet.getNome() == null || pet.getNome().isEmpty() || pet.getNome().trim().equals("")) {
            errors.add("O nome do pet é obrigatório.");
        }
        if (pet.getPeso() == null) {
            errors.add("O peso do pet é obrigatório.");
        } else if (pet.getPeso() <= 0) {
            errors.add("O peso do pet deve ser maior que zero.");
        }
        if (!errors.isEmpty()) {
            response.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok().build();
    }
}