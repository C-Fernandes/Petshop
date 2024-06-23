package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.DTO.PedidoHasProdutoDTO;
import br.com.imd.petshop.DTO.ProdutoListDTO;
import br.com.imd.petshop.Entity.PedidoHasProduto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

   public List<PedidoHasProdutoDTO> listarProdutosPorPedido(String email, Long id) {
        String sql = "SELECT DISTINCT p.id AS pedido_id, p.data, p.valor, pr.id AS produto_id, pr.nome AS produto_nome, preco.valor AS preco_produto,  php.quantidade FROM pedido_has_produto php JOIN pedido p ON php.pedido_id = p.id JOIN produto pr ON php.produto_id = pr.id JOIN produto_has_preco pp ON pp.produto_id = pr.id JOIN preco ON preco.id = pp.preco_id ";
        if (email != null) {
            sql += " WHERE p.cliente_usuario_email = ? ";
        }
       if (id != null) {
           sql += " WHERE p.id = ? ";
       }
        sql += " ORDER BY p.id ";

        List<PedidoHasProdutoDTO> pedidos = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            if (email != null) {
                ps.setString(1, email);
            }

            if (id != null) {
                ps.setLong(1, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                long pedidoAtualId = -1;
                PedidoHasProdutoDTO pedidoAtual = null;

                while (rs.next()) {
                    long pedidoId = rs.getLong("pedido_id");

                    if (pedidoId != pedidoAtualId) {
                        // Novo pedido encontrado
                        pedidoAtual = mapToPedido(rs);
                        pedidoAtual.setProdutos(new ArrayList<>()); // Inicializa a lista de produtos
                        pedidos.add(pedidoAtual);
                        pedidoAtualId = pedidoId;
                    }

                    ProdutoListDTO produto = mapToProduto(rs);
                    pedidoAtual.getProdutos().add(produto);
                }
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
      return  pedidos;
    }

    public void deletarPedidoHasProduto(Long id) {
        String sql = "DELETE php FROM pedido_has_produto php JOIN pedido p ON php.pedido_id = p.id WHERE p.id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PedidoHasProdutoDTO mapToPedido(ResultSet rs) throws SQLException {
        PedidoHasProdutoDTO pedido = new PedidoHasProdutoDTO();
        pedido.setData(rs.getDate("data"));
        pedido.setValor(rs.getDouble("valor"));
        pedido.setPedido_id(rs.getLong("pedido_id"));
        return pedido;
    }

    public ProdutoListDTO mapToProduto(ResultSet rs) throws SQLException {
        ProdutoListDTO produto= new ProdutoListDTO();
        produto.setProduto_id(rs.getLong("produto_id"));
        produto.setQuantidade(rs.getInt("quantidade"));
        produto.setProduto_nome(rs.getString("produto_nome"));
        produto.setProduto_preco(rs.getDouble("preco_produto"));
        produto.setPedido_id(rs.getLong("pedido_id"));
        return produto;
    }
}
