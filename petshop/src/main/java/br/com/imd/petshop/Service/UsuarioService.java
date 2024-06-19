package br.com.imd.petshop.Service;

import br.com.imd.petshop.Entity.Cliente;
import br.com.imd.petshop.Entity.Funcionario;
import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Repository.CepRepository;
import br.com.imd.petshop.Repository.ClienteRepository;
import br.com.imd.petshop.Repository.FuncionarioRepository;
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
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final CepRepository cepRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, CepRepository cepRepository, BCryptPasswordEncoder passwordEncoder,
                          ClienteRepository clienteRepository, FuncionarioRepository funcionarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cepRepository = cepRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
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

    public String login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null && passwordEncoder.matches(senha, usuario.getSenha())) {
            Cliente cliente = clienteRepository.findByUsuario(String.valueOf(usuario));
            if (cliente != null) {
                return "redirect:/cliente/home";
            } else {
                Funcionario funcionario = funcionarioRepository.findByUsuario(String.valueOf(usuario));
                if (funcionario != null) {
                    return "redirect:/funcionario/home";
                }
            }
        }
        return "redirect:/usuario/login";
    }

    public Usuario findUsuario(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
