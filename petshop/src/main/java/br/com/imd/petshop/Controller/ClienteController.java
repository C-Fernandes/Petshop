package br.com.imd.petshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
@RequestMapping("/cliente")
public class ClienteController {

    @GetMapping("/home")
    public String home() {
        return "cliente-home";
    }
}