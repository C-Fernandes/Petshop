package br.com.imd.petshop.Controller;

import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        return "cadastrar-usuario";
    }

    @PostMapping("/cadastro-usuario")
    public String cadastroUsuario(@RequestBody Usuario usuario) {
        usuarioService.cadastrarUsuario(usuario);
        return "redirect:/usuario/login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("senha") String senha) {
        String redirect = usuarioService.login(email, senha);
        return new ModelAndView(redirect);
    }


}
