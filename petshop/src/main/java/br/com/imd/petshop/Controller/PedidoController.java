package br.com.imd.petshop.Controller;

import br.com.imd.petshop.Entity.Pedido;
import br.com.imd.petshop.Service.PedidoService;
import br.com.imd.petshop.Service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("clientes", usuarioService.findAllClientes());
        model.addAttribute("funcionarios", usuarioService.findAllFuncionarios());
        return "cadastrar-pedido";
    }

    @GetMapping("/list")
    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @PostMapping("/create/{funcionarioId}/{clienteId}")
    public String create(@RequestBody Pedido pedido, @PathVariable String funcionarioId, @PathVariable String clienteId) {

        pedidoService.save(pedido);
        return "redirect:/pedido/list";
    }

    @PutMapping("/update/{id}")
    public String update(Long id, Pedido pedido) {
        return "";
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Model model) {
        try {
            pedidoService.delete(id);
            model.addAttribute("msg", "Pedido deletado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg", e.getMessage());
        }
    }
}