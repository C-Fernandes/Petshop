package br.com.imd.petshop.Controller;

import br.com.imd.petshop.DTO.ServicoPrecoDTO;
import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Servico;
import br.com.imd.petshop.Service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servico")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping("/")
    public String index(Model model) {
        return "cadastrar-servico";
    }

    @GetMapping
    public String findAll(Model model) {
        return "";
    }

    @PostMapping("/create")
    public String create(@RequestBody ServicoPrecoDTO servicoPrecoDTO) {
        Servico servico = servicoPrecoDTO.getServico();
        Preco preco = servicoPrecoDTO.getPreco();

        System.out.println(servico.getDescricao());
        System.out.println(servico.getNome());
        System.out.println(servico.getPontosServico());
        System.out.println(preco.getValor());

        servicoService.cadastrar(servico, preco);
        return "redirect:/servico/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        return "";
    }

}
