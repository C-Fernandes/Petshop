package br.com.imd.petshop.Service;

import br.com.imd.petshop.DTO.CarrinhoDTO;
import br.com.imd.petshop.DTO.PedidoDTO;
import br.com.imd.petshop.DTO.PedidoHasProdutoDTO;
import br.com.imd.petshop.DTO.ProdutoListDTO;
import br.com.imd.petshop.Entity.*;
import br.com.imd.petshop.Repository.ClienteRepository;
import br.com.imd.petshop.Repository.FuncionarioRepository;
import br.com.imd.petshop.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public void update(PedidoHasProdutoDTO pedidoAtualizado) {
        List<PedidoHasProdutoDTO> pedido= findByPedidoId(pedidoAtualizado.getPedido_id());
        PedidoHasProdutoDTO pedidoAntigo = pedido.get(0);

        List<ProdutoListDTO> produtosAntigos = pedidoAntigo.getProdutos();
        List<ProdutoListDTO> produtosAtualizados = pedidoAtualizado.getProdutos();

        // Verificar produtos removidos
        for (ProdutoListDTO produtoAntigo : produtosAntigos) {
            boolean encontrado = false;
            for (ProdutoListDTO produtoAtualizado : produtosAtualizados) {
                if (produtoAntigo.getProduto_id() == produtoAtualizado.getProduto_id()) {
                    encontrado = true;
                    break;
                }
            }
            // Se não encontrado, o produtoAntigo foi removido
            if (!encontrado) {
                pedidoHasProdutoService.deletarPedidoHasProduto(produtoAntigo.getPedido_id(), produtoAntigo.getProduto_id());
                System.out.println("Produto removido: " + produtoAntigo.getProduto_id());
            }
        }

        for (ProdutoListDTO produtoAtualizado : produtosAtualizados) {
            boolean encontrado = false;
            for (ProdutoListDTO produtoAntigo : produtosAntigos) {
                if (produtoAtualizado.getProduto_id() == produtoAntigo.getProduto_id()) {
                    encontrado = true;
                    break;
                }
            }
            // Se não encontrado, o produtoAtualizado é novo e deve ser adicionado
            if (!encontrado) {
                PedidoHasProduto hasProduto = new PedidoHasProduto();
                hasProduto.setQuantidade(produtoAtualizado.getQuantidade());

                Produto p = new Produto();
                p.setId(produtoAtualizado.getProduto_id());

                Pedido pedido1 = new Pedido();
                pedido1.setId(pedidoAtualizado.getPedido_id());

                hasProduto.setPedido(pedido1);
                hasProduto.setProduto(p);
                pedidoHasProdutoService.save(hasProduto, pedidoAtualizado.getFuncionarioId(), pedidoAtualizado.getClienteId());
                System.out.println("Novo produto adicionado: " + produtoAtualizado.getProduto_id());
            }
        }
        pedidoAtualizado.setData(new Date());
        updatePedido(pedidoAtualizado.getValor(), pedidoAtualizado.getData(), pedidoAtualizado.getPedido_id());
    }

    public void updatePedido(double valor, Date data, Long pedido_id) {
        pedidoRepository.update(valor, data, pedido_id);
    }

    public void delete (Long id){
        pedidoHasProdutoService.deletarPedidoHasProduto(id);
        pedidoRepository.delete(id);
    }
}
