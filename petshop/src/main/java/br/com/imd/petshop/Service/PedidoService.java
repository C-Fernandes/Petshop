package br.com.imd.petshop.Service;

import br.com.imd.petshop.Entity.Pedido;
import br.com.imd.petshop.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public void save(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public void update(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public void delete(Long id) {
        pedidoRepository.delete(id);
    }
}