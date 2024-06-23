package br.com.imd.petshop.Service;

import br.com.imd.petshop.DTO.PedidoHasProdutoDTO;
import br.com.imd.petshop.Entity.Pedido;
import br.com.imd.petshop.Entity.PedidoHasProduto;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Repository.PedidoHasProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PedidoHasProdutoService {

    @Autowired
    private PedidoHasProdutoRepository pedidoHasProdutoRepository;
    
    public void save(PedidoHasProduto pedidoHasProduto, String func, String cliente) {
        pedidoHasProdutoRepository.save(pedidoHasProduto, func, cliente);
    }

   public List<PedidoHasProdutoDTO> listarPedidoHasProdutos(String email, Long id){
        return pedidoHasProdutoRepository.listarProdutosPorPedido(email, id);
   }

   public void deletarPedidoHasProduto(Long id) {
        pedidoHasProdutoRepository.deletarPedidoHasProduto(id);
   }

   public void deletarPedidoHasProduto(Long idPedido, Long idProduto) {
        pedidoHasProdutoRepository.deletarPedidoHasProduto(idPedido, idProduto);
   }
}
