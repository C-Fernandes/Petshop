package br.com.imd.petshop.Service;

import br.com.imd.petshop.DTO.CarrinhoDTO;
import br.com.imd.petshop.DTO.PedidoDTO;
import br.com.imd.petshop.Entity.*;
import br.com.imd.petshop.Repository.ClienteRepository;
import br.com.imd.petshop.Repository.FuncionarioRepository;
import br.com.imd.petshop.Repository.PedidoRepository;
import br.com.imd.petshop.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoHasProdutoService pedidoHasProdutoService;

    @Autowired
    private ProdutoService produtoService;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public void save(PedidoDTO pedido) {
        Pedido pedidoSave = new Pedido();
        pedidoSave.setValor(pedido.getValor());
        pedidoSave.setData(pedido.getData());
        pedidoSave.setStatus(pedido.getStatus());

        List<Usuario> funcionarios = usuarioRepository.findAllFuncionarios();
        List<Usuario> clientes = usuarioRepository.findAllClientes();
        Usuario cliente = new Usuario();
        Usuario funcionario = new Usuario();

        for(Usuario usuario : funcionarios) {
            if(usuario.getEmail().equals(pedido.getFuncionarioId())) {
                funcionario = usuario;
            }
        }

        for(Usuario usuario : clientes) {
            if(usuario.getEmail().equals(pedido.getClienteId())) {
                cliente = usuario;
            }
        }

        if(funcionario != null) {
            pedidoSave.setFuncionario((Funcionario) funcionario);
            System.out.println(pedidoSave.getFuncionario());
        }

        if(cliente != null) {
            pedidoSave.setCliente((Cliente) cliente);
        }

        pedidoRepository.save(pedidoSave);

        List<Produto> produtos = produtoService.findAll();
        List<CarrinhoDTO> carrinho = pedido.getCarrinhoDTO();
        List<PedidoHasProduto> pedidoHasProdutos = new ArrayList<>();
        Pedido prod = findByParams(pedidoSave);

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
            pedidoHasProdutoService.save(p);
        }
    }

    public Pedido findByParams(Pedido pedido) {
        return pedidoRepository.findByParams(pedido);
    }

    public void update(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public void delete(Long id) {
        pedidoRepository.delete(id);
    }
}