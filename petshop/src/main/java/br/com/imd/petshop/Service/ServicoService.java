package br.com.imd.petshop.Service;

import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Entity.Servico;
import br.com.imd.petshop.Repository.PrecoRepository;
import br.com.imd.petshop.Repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private PrecoRepository precoRepository;

    public List<Servico> listar() {
        return servicoRepository.findAll();
    }

    public Servico buscar(Long id) {
        return null;
    }

    public void cadastrar(Servico servico, Preco preco) {
        java.sql.Date dataAtual = new Date(System.currentTimeMillis());

        try {
            Preco precoExistente = precoRepository.findbyDateEValor(dataAtual, preco.getValor());
            System.out.println("Precos");
            System.out.println(preco.getValor());

            if (precoExistente == null) {
                precoExistente = precoRepository.save(preco);
            } else {
                System.out.println(precoExistente.getValor());
            }
            Servico servico1 = servicoRepository.save(servico);
            servicoRepository.saveProdutoAndPreco(servico1, precoExistente);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void deletar(Long id) {
        servicoRepository.delete(id);
    }
}
