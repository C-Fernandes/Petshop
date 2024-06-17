package br.com.imd.petshop.Service;

import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

import java.sql.Date;
import java.time.ZoneId;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void cadastrarUsuario(Usuario usuario) {
        // Obtém a data atual
        LocalDate dataAtual = LocalDate.now();

        // Converte a data de nascimento para LocalDate
        LocalDate dataNascimento = usuario.getDataDeNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Calcula a diferença entre as datas
        Period periodo = Period.between(dataNascimento, dataAtual);

        // Obtém a idade a partir do período
        int idade = periodo.getYears();

        // Define a idade do usuário
        usuario.setIdade(idade);

        // Salva o usuário no banco de dados
        usuarioRepository.save(usuario);
    }
}
