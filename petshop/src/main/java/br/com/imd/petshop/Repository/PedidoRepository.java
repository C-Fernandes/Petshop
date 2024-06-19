package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.Cliente;
import br.com.imd.petshop.Entity.Funcionario;
import br.com.imd.petshop.Entity.Pedido;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PedidoRepository {

    public void save(Pedido pedido) {
        String sql = "INSERT INTO pedido () VALUES (valor, data, status, funcionario_usuario_email, cliente_usuario_email)";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, pedido.getValor());
            ps.setDate(2, new java.sql.Date(pedido.getData().getTime()));
            ps.setString(3, pedido.getStatus());
            ps.setObject(4, pedido.getFuncionario().getUsuario().getEmail());
            ps.setObject(5, pedido.getCliente().getUsuario().getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> findAll() {
        String sql = "SELECT * FROM pedido";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = mapRow(rs);
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM pedido WHERE id = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Pedido mapRow(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setValor(rs.getDouble("valor"));
        pedido.setData(rs.getDate("data"));
        pedido.setStatus(rs.getString("status"));
        pedido.setFuncionario(rs.getObject("funcionario", Funcionario.class));
        pedido.setCliente(rs.getObject("cliente", Cliente.class));
        return pedido;
    }
}