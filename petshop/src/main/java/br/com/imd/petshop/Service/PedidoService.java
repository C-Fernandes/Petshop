package br.com.imd.petshop.Service;

import br.com.imd.petshop.DTO.CarrinhoDTO;
import br.com.imd.petshop.DTO.PedidoDTO;
import br.com.imd.petshop.DTO.PedidoHasProdutoDTO;
import br.com.imd.petshop.Entity.*;
import br.com.imd.petshop.Repository.ClienteRepository;
import br.com.imd.petshop.Repository.FuncionarioRepository;
import br.com.imd.petshop.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PedidoHasProdutoService pedidoHasProdutoService;

    @Autowired
    private ProdutoService produtoService;

    public List<PedidoHasProdutoDTO> findaAll(String email, Long id) {
        return pedidoHasProdutoService.listarPedidoHasProdutos(email, id);
    }

    public List<PedidoHasProdutoDTO> findByPedidoId(Long id) {
        return pedidoHasProdutoService.listarPedidoHasProdutos(null, id);
    }

    public void save(PedidoDTO pedido) {
        Pedido pedidoSave = new Pedido();
        pedidoSave.setValor(pedido.getValor());
        pedidoSave.setData(pedido.getData());
        pedidoSave.setStatus(pedido.getStatus());

        Cliente cliente = clienteRepository.findByEmail(pedido.getFuncionarioId());
        Funcionario funcionario = funcionarioRepository.findByEmail(pedido.getClienteId());

        pedidoSave.setCliente(cliente);
        pedidoSave.setFuncionario(funcionario);

        Pedido prod = pedidoRepository.save(pedidoSave, pedido.getFuncionarioId(), pedido.getClienteId());

        List<Produto> produtos = produtoService.findAll();
        List<CarrinhoDTO> carrinho = pedido.getCarrinhoDTO();
        List<PedidoHasProduto> pedidoHasProdutos = new ArrayList<>();

        for(CarrinhoDTO carrinhoDTO : carrinho) {
            for (Produto p : produtos) {
                if(p.getId().equals(carrinhoDTO.getId())) {
                    PedidoHasProduto hasProduto = new PedidoHasProduto();
                    hasProduto.setQuantidade(carrinhoDTO.getQuantidade());
                    hasProduto.setProduto(p);
                    hasProduto.setPedido(prod);
                    pedidoHasProdutos.add(hasProduto);
                }
            }
        }

        for(PedidoHasProduto p : pedidoHasProdutos) {
            pedidoHasProdutoService.save(p, pedido.getFuncionarioId(), pedido.getClienteId());
        }
    }

    public void update(Pedido pedido) {
       // pedidoRepository.save(pedido);
    }

    public void delete (Long id){
        pedidoHasProdutoService.deletarPedidoHasProduto(id);
        pedidoRepository.delete(id);
    }
}
