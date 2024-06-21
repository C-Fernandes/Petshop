package br.com.imd.petshop.Service;

import br.com.imd.petshop.DTO.UsuarioDTO;
import br.com.imd.petshop.Entity.Cliente;
import br.com.imd.petshop.Entity.Funcionario;
import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Repository.CepRepository;
import br.com.imd.petshop.Repository.ClienteRepository;
import br.com.imd.petshop.Repository.FuncionarioRepository;
import br.com.imd.petshop.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    private static final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final CepRepository cepRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, CepRepository cepRepository, BCryptPasswordEncoder passwordEncoder,
                          ClienteRepository clienteRepository, FuncionarioRepository funcionarioRepository) {
        this.cepRepository = cepRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public void cadastrarUsuario(UsuarioDTO usuarioDto) {

        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setSenha(usuarioDto.getSenha());
        usuario.setNome(usuarioDto.getNome());
        usuario.setDataDeNascimento(usuarioDto.getDataDeNascimento());
        usuario.setTelefone(usuarioDto.getTelefone());
        usuario.setLogradouro(usuarioDto.getLogradouro());
        usuario.setNumero(usuarioDto.getNumero());
        usuario.setBairro(usuarioDto.getBairro());
        usuario.setCep(usuarioDto.getCep());

        String cepValue = usuario.getCep().getCep();
        if (!cepRepository.existsById(cepValue)) {
            cepRepository.save(usuario.getCep());
        }

        LocalDate dataAtual = LocalDate.now();
        LocalDate dataNascimento = usuario.getDataDeNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period periodo = Period.between(dataNascimento, dataAtual);
        int idade = periodo.getYears();
        usuario.setIdade(idade);

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuarioRepository.save(usuario);

        if (usuarioDto.getCargo() == null) {
            Cliente cliente = new Cliente();
            cliente.setUsuario(usuario);
            cliente.setQtdPontos(0L);
            clienteRepository.save(cliente);
        }
        if (usuarioDto.getQtdPontos() == null){
            Funcionario funcionario = new Funcionario();
            funcionario.setUsuario(usuario);
            funcionario.setCargo(usuarioDto.getCargo());
            funcionarioRepository.save(funcionario);
        }
    }
    public ResponseEntity<Map<String, Object>> validarCamposObrigatorios(UsuarioDTO usuarioDTO) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (usuarioDTO.getNome() == null || usuarioDTO.getNome().isEmpty()) {
            errors.add("Nome é um campo obrigatório.");
        }
        if (usuarioDTO.getTelefone() == null || usuarioDTO.getTelefone().isEmpty()) {
            errors.add("Telefone é um campo obrigatório.");
        }
        if (usuarioDTO.getLogradouro() == null || usuarioDTO.getLogradouro().isEmpty()) {
            errors.add("Logradouro é um campo obrigatório.");
        }
        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isEmpty()) {
            errors.add("E-mail é um campo obrigatório.");
        }
        if (usuarioDTO.getBairro() == null || usuarioDTO.getBairro().isEmpty()) {
            errors.add("Bairro é um campo obrigatório.");
        }
        if (usuarioDTO.getSenha() == null || usuarioDTO.getSenha().isEmpty()) {
            errors.add("Senha é um campo obrigatório.");
        }
        if (usuarioDTO.getNumero() == null) {
            errors.add("Número é um campo obrigatório.");
        }
        if (usuarioDTO.getDataDeNascimento() == null) {
            errors.add("Data de nascimento é um campo obrigatório.");
        }
        if (usuarioDTO.getCep() == null || usuarioDTO.getCep().isEmpty()) {
            errors.add("Cep é um campo obrigatório.");
            if(usuarioDTO.getCep().getCidade() == null || usuarioDTO.getCep().getCidade().isEmpty()) {
                errors.add("Cidade é um campo obrigatório.");
            }
            if(usuarioDTO.getCep().getEstado() == null || usuarioDTO.getCep().getEstado().isEmpty()) {
                errors.add("Estado é um campo obrigatório.");
            }
        } else {
            if (usuarioDTO.getCep().getCidade() == null || usuarioDTO.getCep().getCidade().isEmpty()) {
                errors.add("Cidade é um campo obrigatório.");
            }
            if (usuarioDTO.getCep().getEstado() == null || usuarioDTO.getCep().getEstado().isEmpty()) {
                errors.add("Estado é um campo obrigatório.");
            }
        }
        if (usuarioDTO.getEmail() != null && usuarioRepository.findByEmail(usuarioDTO.getEmail()) != null) {
            errors.add("Este e-mail já está cadastrado em nosso site.");
        }
        if (usuarioDTO.getEmail() != null && !isValidEmail(usuarioDTO.getEmail())) {
            errors.add("E-mail inválido.");
        }
        if (usuarioDTO.getSenha() != null && usuarioDTO.getSenha().length() <= 7) {
            errors.add("A senha deve ter no mínimo 7 caracteres.");
        }
        if (!errors.isEmpty()) {
            response.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }


        return ResponseEntity.ok().build();
    }



    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
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
