package br.com.imd.petshop.Controller;

import br.com.imd.petshop.DTO.UsuarioDTO;
import br.com.imd.petshop.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin("*")
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastrar-usuario-cliente";
    }

    @PostMapping("/cadastro-usuario")
    public ResponseEntity<Map<String, Object>> cadastrarUsuario(UsuarioDTO usuarioDto) {
        ResponseEntity<Map<String, Object>> validationResponse = usuarioService.validarCamposObrigatorios(usuarioDto);

        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().body(validationResponse.getBody());
        }

        try {
            usuarioService.validarUsuario(usuarioDto);
        } catch (IllegalArgumentException e) {
            // Tratar a exceção aqui, por exemplo, retornando um ResponseEntity com os erros
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

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("senha") String senha) {
        String redirect = usuarioService.login(email, senha);
        return new ModelAndView(redirect);
    }

    @GetMapping("/inicial")
    public String inicial() { return  "tela-inicial"; }


}