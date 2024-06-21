package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.PedidoHasProduto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;


@Repository
public class PedidoHasProdutoRepository {

    public void save(PedidoHasProduto pedidoHasProduto, String func, String cliente) {
        String sql = "INSERT INTO pedido_has_produto (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Long.valueOf(pedidoHasProduto.getPedido().getId()).intValue());
            ps.setInt(2, Long.valueOf(pedidoHasProduto.getProduto().getId()).intValue());
            ps.setInt(3, pedidoHasProduto.getQuantidade());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
