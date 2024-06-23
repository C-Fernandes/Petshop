package br.com.imd.petshop.Controller;

import br.com.imd.petshop.DTO.CarrinhoDTO;
import br.com.imd.petshop.DTO.PedidoDTO;
import br.com.imd.petshop.DTO.PedidoHasProdutoDTO;
import br.com.imd.petshop.Entity.Pedido;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Repository.PedidoHasProdutoRepository;
import br.com.imd.petshop.Service.PedidoHasProdutoService;
import br.com.imd.petshop.Service.PedidoService;
import br.com.imd.petshop.Service.ProdutoService;
import br.com.imd.petshop.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private PedidoHasProdutoService pedidoHasProdutoService;

    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<PedidoHasProdutoDTO> pedidoEmEdicao = (List<PedidoHasProdutoDTO>) session.getAttribute("pedidoEmEdicao");

        if (pedidoEmEdicao != null) {
            System.out.println("pedido id" + pedidoEmEdicao.get(0).getPedido_id());
            model.addAttribute("pedidoEmEdicao", pedidoEmEdicao); // Use a descriptive attribute name
        }
        model.addAttribute("clientes", usuarioService.findAllClientes());
        model.addAttribute("funcionarios", usuarioService.findAllFuncionarios());
        model.addAttribute("produtos", produtoService.findAll());
        model.addAttribute("produto", new Produto());
        return "cadastrar-pedido";
    }

    @GetMapping("/list")
    public String findAll(Model model, @RequestParam(required = false) String email) {
        model.addAttribute("pedidos", pedidoHasProdutoService.listarPedidoHasProdutos(email, null));
        return "listagem-pedidos";
    }

    @PostMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,  HttpSession session) {
        List<PedidoHasProdutoDTO> pedido = pedidoService.findByPedidoId(id);
        session.setAttribute("pedidoEmEdicao", pedido); // Set a meaningful attribute name
        model.addAttribute("pedido", pedido);
        return "redirect:/pedido/";
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
    public ResponseEntity<?> delete(@PathVariable String id, Model model) {
        try {
            Long idPedido = Long.parseLong(id);
            pedidoService.delete(idPedido);
            return ResponseEntity.ok("Pedido deletado com sucesso");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("ID de pedido inválido: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir pedido: " + e.getMessage());
        }
    }
}