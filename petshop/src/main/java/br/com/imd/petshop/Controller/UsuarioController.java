package br.com.imd.petshop.Controller;

import br.com.imd.petshop.DTO.ClienteDTO;
import br.com.imd.petshop.DTO.FuncionarioDTO;
import br.com.imd.petshop.DTO.UsuarioDTO;
import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Entity.UsuarioLogado;
import br.com.imd.petshop.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioLogado usuarioLogado;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioLogado usuarioLogado) {
        this.usuarioService = usuarioService;
        this.usuarioLogado = usuarioLogado;
    }


    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastrar-usuario-cliente";
    }

    @GetMapping("/cadastro-funcionario")
    public String cadastroFuncionario() {
        return "cadastrar-usuario-funcionario";
    }

    @PostMapping("/cadastro-usuario")
    public ResponseEntity<Map<String, Object>> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDto) {
        ResponseEntity<Map<String, Object>> validationResponse = usuarioService.validarCamposObrigatorios(usuarioDto);

        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().body(validationResponse.getBody());
        }

        try {
            usuarioService.cadastrarUsuario(usuarioDto);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("errors", Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return null;
    }

    @GetMapping("/login")
    public String login() {
        return "tela-login";
    }

    @PostMapping("/logar")
    public String login(@RequestParam("email") String email, @RequestParam("senha") String senha, Model model) {
        String redirect = usuarioService.login(email, senha);
        if (redirect.equals("redirect:/")) {
            model.addAttribute("erro", "Usuário ou senha inválidos");
            return "tela-login";
        }
        return redirect;
    }


    @GetMapping("/inicial")
    public String inicial() {
        return "tela-inicial";
    }

    @GetMapping("/listagem")
    public String listarUsuarios(Model model) {
        List<ClienteDTO> clientes = usuarioService.listarClientesComUsuarios();
        List<FuncionarioDTO> funcionarios = usuarioService.listarFuncionariosComUsuarios();
        model.addAttribute("clientes", clientes);
        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("email", usuarioLogado.getEmail());
        return "listagem-usuario";
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> obterUsuarioPorEmail(@PathVariable String email) {
        ClienteDTO clienteDTO = usuarioService.obterClienteDTO(email);
        if (clienteDTO != null) {
            return ResponseEntity.ok(clienteDTO);
        }

        FuncionarioDTO funcionarioDTO = usuarioService.obterFuncionarioDTO(email);
        if (funcionarioDTO != null) {
            return ResponseEntity.ok(funcionarioDTO);
        }

        return ResponseEntity.notFound().build();
    }
    @PutMapping("/editar")
    public ResponseEntity<Map<String, Object>> editarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        ResponseEntity<Map<String, Object>> validationResponse = usuarioService.validarCamposObrigatoriosEdicao(usuarioDTO);

        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().body(validationResponse.getBody());
        }

        try {
            usuarioService.editarUsuario(usuarioDTO);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("errors", Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok().build();
    }
    @PutMapping("/desativar")
    public ResponseEntity<?> desativarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        usuarioService.desativarUsuario(usuarioDTO.getEmail());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrarUsuarios(@RequestParam String tipo) {
        if (tipo.equals("cliente")) {
            List<ClienteDTO> clientes = usuarioService.listarClientesComUsuarios();
            return ResponseEntity.ok(clientes);
        } else if (tipo.equals("funcionario")) {
            List<FuncionarioDTO> funcionarios = usuarioService.listarFuncionariosComUsuarios();
            return ResponseEntity.ok(funcionarios);
        } else {
            List<ClienteDTO> clientes = usuarioService.listarClientesComUsuarios();
            List<FuncionarioDTO> funcionarios = usuarioService.listarFuncionariosComUsuarios();
            Map<String, List<?>> todosUsuarios = new HashMap<>();
            todosUsuarios.put("clientes", clientes);
            todosUsuarios.put("funcionarios", funcionarios);
            return ResponseEntity.ok(todosUsuarios);
        }
    }

    @GetMapping("/meus-dados")
    public String meusDados(Model model) {
        UsuarioDTO usuarioDTO = usuarioService.obterUsuarioDTO(usuarioLogado.getEmail());
        model.addAttribute("usuario", usuarioDTO);
        return "meu-usuario";
    }

}