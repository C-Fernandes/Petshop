package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
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

    public Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        Usuario usuario = new Usuario();
        usuario.setEmail(rs.getString("email"));
        cliente.setUsuario(usuario);
        cliente.setQtdPontos(rs.getLong("qtd_pontos"));
        return cliente;
    }


}
