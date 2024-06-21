package br.com.imd.petshop.Controller;

import br.com.imd.petshop.DTO.CarrinhoDTO;
import br.com.imd.petshop.DTO.PedidoDTO;
import br.com.imd.petshop.Entity.Pedido;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Service.PedidoService;
import br.com.imd.petshop.Service.ProdutoService;
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
    private final ProdutoService produtoService;

    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("clientes", usuarioService.findAllClientes());
        model.addAttribute("funcionarios", usuarioService.findAllFuncionarios());
        model.addAttribute("produtos", produtoService.findAll());
        model.addAttribute("produto", new Produto());
        return "cadastrar-pedido";
    }

    @GetMapping("/list")
    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @PostMapping("/create/{funcionarioId}/{clienteId}")
    public String create(@RequestBody PedidoDTO pedido, @PathVariable String funcionarioId, @PathVariable String clienteId) {
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