package br.com.imd.petshop.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Service.ProdutoService;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/uploads/produtos/";

    @GetMapping("/")
    public String cadastro(Model model) {
        List<Produto> produtos = produtoService.findAll();
        model.addAttribute("produtos", produtos);
        return "produtos-gerenciamento";
    }

    @PostMapping("/cadastro-produto")
    public ResponseEntity<?> cadastrarProduto(@RequestParam("file") MultipartFile file,
            @RequestParam("produto") String produtoJson,
            @RequestParam("preco") String precoJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Produto produto = objectMapper.readValue(produtoJson, Produto.class);
            Preco preco = objectMapper.readValue(precoJson, Preco.class);

            // Verificar se a imagem recebida é a imagem padrão
            String nomeImagem = file.getOriginalFilename();
            String nomeImagemPadrao = "padrao.jpeg"; // Nome da imagem padrão

            String urlImagem = "";

            if (nomeImagem != null && nomeImagem.equals(nomeImagemPadrao)) {
                // Utilizar a URL da imagem padrão sem salvar no disco novamente
                urlImagem = nomeImagemPadrao;
                produto.setImagem(nomeImagemPadrao);
            } else {
                // Salvar a imagem no disco
                urlImagem = produtoService.salvarNovaImagem(file, UPLOAD_DIR);
                System.out.println("urlImagem cadastro:" + urlImagem);
                produto.setImagem(urlImagem);
            }

            produtoService.cadastrarProduto(preco, produto);

            // Construir o objeto JSON de resposta com a mensagem e a URL da imagem
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Produto cadastrado com sucesso!");
            response.put("imagem", urlImagem);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Não foi possível cadastrar o produto");
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarProduto(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("produto") String produtoJson,
            @RequestParam("preco") String precoJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Produto produto = objectMapper.readValue(produtoJson, Produto.class);
            Preco preco = objectMapper.readValue(precoJson, Preco.class);

            Long idProduto = produto.getId(); // Obtém o ID do produto do objeto Produto

            // Verifica se o ID do produto no corpo da requisição é válido
            if (idProduto == null) {
                return ResponseEntity.badRequest().body("ID do produto não especificado");
            }

            // Se uma nova imagem foi enviada, processa e salva como no cadastro
            if (file != null && !file.isEmpty()) {

                // Salva a nova imagem
                String urlImagem = produtoService.salvarNovaImagem(file, UPLOAD_DIR);
                System.out.println(urlImagem);

                produtoService.removerImagem(produto.getImagem(), UPLOAD_DIR);
                produto.setImagem(urlImagem);

            } else {
                // Se nenhum novo arquivo de imagem foi enviado, mantém a imagem existente
                Produto produtoExistente = produtoService.findById(idProduto);
                if (produtoExistente != null) {
                    produto.setImagem(produtoExistente.getImagem());
                }
            }

            // Atualiza o produto
            produtoService.atualizarProduto(produto, preco);

            // Prepara a resposta com a URL da imagem atualizada, se disponível
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Produto atualizado com sucesso!");
            response.put("imagem", produto.getImagem()); // Adiciona a URL da imagem ao objeto de resposta

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao atualizar o produto");
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
            Produto produto = produtoService.findById(id);

            if (produto != null && produto.getImagem() != null) {
                produtoService.removerImagem(produto.getImagem(), UPLOAD_DIR); // Remove a imagem associada
            }

            // Deletar o produto do banco de dados (incluindo relações dependentes, se
            // houver)
            produtoService.deletarProduto(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao deletar o produto");
        }
    }
}