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

    public List<Produto> atualizarPreco(List<Produto> produtos) {

        for (Produto produto : produtos) {

            List<Preco> precosProduto = precoRepository.findByProduto(produto.getId());

            long menorQuantidadeDias = Long.MAX_VALUE;
            Preco precoMenorQuantidadeDias = new Preco();

            for (Preco preco : precosProduto) {
                System.out.println("preco retornado: " + preco.getValor());
                long diasPassados = ChronoUnit.DAYS.between(new Date(preco.getData().getTime()).toLocalDate(),
                        LocalDate.now());

                System.out.println("Menor quantidade de dias " + menorQuantidadeDias);
                System.out.println("dias passados: " + diasPassados);

                if (diasPassados < menorQuantidadeDias) {
                    menorQuantidadeDias = diasPassados;
                    precoMenorQuantidadeDias.setId(preco.getId());
                    precoMenorQuantidadeDias.setData(preco.getData());
                    precoMenorQuantidadeDias.setValor(preco.getValor());
                }
            }
            System.out.println("preco com menor quantidade de dias;" + precoMenorQuantidadeDias.getValor());
            produto.setPreco(precoMenorQuantidadeDias);
        }
        return produtos;

    }

    public void cadastrarProduto(Preco preco, Produto produto) {
        try {
            Date dataAtual = new Date(System.currentTimeMillis());
            Preco precoExistente = precoRepository.findbyDateEValor(dataAtual, preco.getValor());
            System.out.println();
            if (precoExistente == null) {
                precoExistente = precoRepository.save(preco);
            }
            System.out.println(precoExistente.getId());
            Produto prod = produtoRepository.saveProdutoEPreco(produto);
            produtoRepository.salvarRelacionamento(prod, precoExistente);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public List<Produto> findAll() {
        List<Produto> produtos = atualizarPreco(produtoRepository.findAll());
        return produtos;
    }
}
