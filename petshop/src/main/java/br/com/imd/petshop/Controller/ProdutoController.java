package br.com.imd.petshop.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.imd.petshop.DTO.ProdutoPrecoDTO;
import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Service.ProdutoService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    ProdutoService produtoService;

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        List<Produto> produtos = produtoService.findAll();
        model.addAttribute("produtos", produtos);
        return "cadastrar-produto";
    }

    @PostMapping("/cadastro-produto")
    public ResponseEntity<String> cadastrarProduto(
            @RequestBody ProdutoPrecoDTO dados) {

        try {
            produtoService.cadastrarProduto(new Preco(dados.getPreco()), dados.getProduto());

            return ResponseEntity.ok("Produto cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Não foi possível cadastrar o produto");
        }
    }

}
