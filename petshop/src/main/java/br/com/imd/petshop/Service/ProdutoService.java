package br.com.imd.petshop.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import java.util.List;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Repository.PrecoRepository;
import br.com.imd.petshop.Repository.ProdutoRepository;

@Service
public class ProdutoService {
    @Autowired
    PrecoRepository precoRepository;
    @Autowired
    ProdutoRepository produtoRepository;
    Date dataAtual = new Date(System.currentTimeMillis());

    public Preco precoMaisRecente(List<Preco> precos) {

        long menorQuantidadeDias = Long.MAX_VALUE;
        Preco precoMenorQuantidadeDias = new Preco();

        for (Preco preco : precos) {
            long diasPassados = ChronoUnit.DAYS.between(new Date(preco.getData().getTime()).toLocalDate(),
                    LocalDate.now());

            if (diasPassados <= menorQuantidadeDias) {
                menorQuantidadeDias = diasPassados;
                precoMenorQuantidadeDias.setId(preco.getId());
                precoMenorQuantidadeDias.setData(preco.getData());
                precoMenorQuantidadeDias.setValor(preco.getValor());
            }
        }
        return precoMenorQuantidadeDias;
    }

    public List<Produto> atualizarPreco(List<Produto> produtos) {

        for (Produto produto : produtos) {

            List<Preco> precosProduto = precoRepository.findByProduto(produto.getId());
            produto.setPreco(precoMaisRecente(precosProduto));
        }
        return produtos;

    }

    public void cadastrarProduto(Preco preco, Produto produto) {
        try {
            Preco precoExistente = precoRepository.findbyDateEValor(dataAtual, preco.getValor());
            System.out.println("Precos");
            System.out.println(preco.getValor());

            if (precoExistente == null) {
                precoExistente = precoRepository.save(preco);
            } else {
                System.out.println(precoExistente.getValor());

            }
            Produto prod = produtoRepository.saveProdutoEPreco(produto);
            produtoRepository.salvarRelacionamento(prod, precoExistente);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void deletarProduto(Long idProduto) {
        List<Preco> precos = precoRepository.findByProduto(idProduto);
        produtoRepository.deleteProduto(idProduto);

        for (Preco p : precos) {
            System.out.println(p.getId());
            System.out.println(!precoRepository.verificarProdutoHasPreco(p.getId()));
            if (!precoRepository.verificarProdutoHasPreco(p.getId())) {
                precoRepository.deletarPreco(p.getId());
            }
        }
    }

    public List<Produto> findAll() {
        List<Produto> produtos = atualizarPreco(produtoRepository.findAll());

        return produtos;
    }

    public void atualizarProduto(Produto produto, Preco preco) {
        Produto pVelho = produtoRepository.findbyId(produto.getId());
        System.out.println("Valor velh9:" + preco.getValor());
        System.out.println(precoMaisRecente(precoRepository.findByProduto(pVelho.getId())).getValor());
        if (preco.getValor() != precoMaisRecente(precoRepository.findByProduto(pVelho.getId())).getValor()) {
            produtoRepository.salvarRelacionamento(produto, precoRepository.save(preco));
        }
        produtoRepository.atualizarProduto(produto);
    }
}
