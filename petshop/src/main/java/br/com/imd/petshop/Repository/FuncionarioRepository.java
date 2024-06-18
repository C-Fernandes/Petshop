package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.Funcionario;
import br.com.imd.petshop.Entity.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class FuncionarioRepository {
    public Funcionario findByUsuario(String usuario) {
        String sql = "SELECT * FROM funcionario WHERE email = ?";
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

    public Funcionario mapRow(ResultSet rs) throws SQLException {
        Funcionario funcionario = new Funcionario();
        Usuario usuario = new Usuario();
        usuario.setEmail(rs.getString("email"));
        funcionario.setUsuario(usuario);
        funcionario.setCargo(rs.getString("cargo"));
        return funcionario;
    }
}
