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
import java.util.Date;

@Repository
public class PedidoRepository {

    public Pedido save(Pedido pedido, String func, String cliente) {
        String sql = "INSERT INTO pedido (valor, data, status, funcionario_usuario_email, cliente_usuario_email) VALUES (?, ?, ?, ?, ?) ";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, pedido.getValor());
            ps.setDate(2, new java.sql.Date(pedido.getData().getTime()));
            ps.setString(3, pedido.getStatus());
            ps.setString(4, func);
            ps.setString(5, cliente);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("A inserção do pedido falhou, nenhuma linha foi afetada.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedido.setId(generatedKeys.getLong(1));
                    return pedido;
                } else {
                    throw new SQLException("A inserção do pedido falhou, nenhum ID foi gerado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public void update(double valor, Date data, Long id) {
        String sql = "UPDATE pedido SET valor = ?, data = ? WHERE id = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, valor);
            ps.setDate(2, new java.sql.Date(new Date().getTime()));
            ps.setLong(3, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Pedido mapRow(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getLong("id"));
        pedido.setValor(rs.getDouble("valor"));
        pedido.setData(rs.getDate("data"));
        pedido.setStatus(rs.getString("status"));
        pedido.setFuncionario(rs.getObject("funcionario", Funcionario.class));
        pedido.setCliente(rs.getObject("cliente", Cliente.class));
        return pedido;
    }


    private Pedido mapRowList(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getLong("id"));
        pedido.setValor(rs.getDouble("valor"));
        pedido.setData(rs.getDate("data"));
        pedido.setStatus(rs.getString("status"));
        return pedido;
    }
}