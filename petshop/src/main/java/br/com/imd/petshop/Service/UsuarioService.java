package br.com.imd.petshop.Service;

import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Repository.CepRepository;
import br.com.imd.petshop.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CepRepository cepRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, CepRepository cepRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.cepRepository = cepRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void cadastrarUsuario(Usuario usuario) {
        String cepValue = usuario.getCep().getCep();
        if (!cepRepository.existsById(cepValue)) {
            cepRepository.save(usuario.getCep());
        }
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataNascimento = usuario.getDataDeNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Period periodo = Period.between(dataNascimento, dataAtual);

        int idade = periodo.getYears();
        usuario.setIdade(idade);

        // Criptografar a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuarioRepository.save(usuario);
    }

    public boolean login(Usuario usuario) {
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(usuario.getEmail());
        return usuarioEncontrado != null && passwordEncoder.matches(usuario.getSenha(), usuarioEncontrado.getSenha());
    }
}
