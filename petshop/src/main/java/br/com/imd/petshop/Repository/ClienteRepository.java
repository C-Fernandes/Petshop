package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.Cep;
import br.com.imd.petshop.Entity.Cliente;
import br.com.imd.petshop.Entity.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ClienteRepository {
    public Cliente findByUsuario(String usuario) {
        String sql = "SELECT * FROM cliente WHERE email = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cliente findByEmail(String email) {
        String sql = "SELECT c.*, u.* FROM cliente c " +
                "INNER JOIN usuario u ON c.usuario_email = u.email " +
                "WHERE usuario_email = ?";
        Cliente cliente = new Cliente();
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = mapRowUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        Usuario usuario = new Usuario();
        usuario.setEmail(rs.getString("email"));
        cliente.setUsuario(usuario);
        cliente.setQtdPontos(rs.getLong("qtd_pontos"));
        return cliente;
    }

    public Cliente mapRowUsuario(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        Usuario usuario = new Usuario();
        usuario.setEmail(rs.getString("email"));
        usuario.setNome(rs.getString("nome"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setBairro(rs.getString("bairro"));
        usuario.setLogradouro(rs.getString("logradouro"));
        usuario.setNumero(rs.getLong("numero"));
        usuario.setDataDeNascimento(rs.getDate("data_nascimento"));
        usuario.setIdade(rs.getInt("idade"));
        cliente.setQtdPontos(rs.getLong("qtd_pontos"));

        cliente.setEmail(rs.getString("email"));
        cliente.setNome(rs.getString("nome"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setBairro(rs.getString("bairro"));
        cliente.setLogradouro(rs.getString("logradouro"));
        cliente.setNumero(rs.getLong("numero"));
        cliente.setDataDeNascimento(rs.getDate("data_nascimento"));
        cliente.setIdade(rs.getInt("idade"));
        cliente.setQtdPontos(rs.getLong("qtd_pontos"));

        cliente.setUsuario(usuario);

        return cliente;
    }

}
