package br.com.imd.petshop.Controller;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public ResponseEntity<String> cadastrarProduto(@RequestBody ProdutoPrecoDTO dados) {
        try {
            Produto produto = dados.getProduto();
            Preco preco = dados.getPreco();
            produtoService.cadastrarProduto(preco, produto);
            return ResponseEntity.ok("Produto cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Não foi possível cadastrar o produto");
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarProduto(@RequestBody ProdutoPrecoDTO dados) {
        try {
            Produto p = dados.getProduto();
            produtoService.atualizarProduto(dados.getProduto(), dados.getPreco());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listar")
    @ResponseBody
    public ResponseEntity<List<Produto>> listarProdutos() {
        try {
            List<Produto> produtos = produtoService.findAll();
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarProduto(@PathVariable Long id) {
        try {
            System.out.println("Entrou em deletar produto");
            produtoService.deletarProduto(id); // Chama o serviço para deletar o produto
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
