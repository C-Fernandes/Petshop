package br.com.imd.petshop.Service;

import br.com.imd.petshop.Entity.PedidoHasProduto;
import br.com.imd.petshop.Repository.PedidoHasProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoHasProdutoService {

    @Autowired
    private PedidoHasProdutoRepository pedidoHasProdutoRepository;
    
    public void save(PedidoHasProduto pedidoHasProduto, String func, String cliente) {
        pedidoHasProdutoRepository.save(pedidoHasProduto, func, cliente);
    }
}
