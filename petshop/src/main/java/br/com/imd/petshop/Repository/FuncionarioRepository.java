package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.Cliente;
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


    public Funcionario findByEmail(String email) {
        String sql = "SELECT f.*, u.* FROM funcionario f " +
                "INNER JOIN usuario u ON f.usuario_email = u.email " +
                "WHERE usuario_email = ?";
        Funcionario funcionario = new Funcionario();
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    funcionario = mapRowUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionario;
    }

    public Funcionario mapRow(ResultSet rs) throws SQLException {
        Funcionario funcionario = new Funcionario();
        Usuario usuario = new Usuario();
        usuario.setEmail(rs.getString("email"));
        funcionario.setUsuario(usuario);
        funcionario.setCargo(rs.getString("cargo"));
        return funcionario;
    }

    public Funcionario mapRowUsuario(ResultSet rs) throws SQLException {
        Funcionario funcionario = new Funcionario();
        Usuario usuario = new Usuario();
        usuario.setEmail(rs.getString("email"));
        usuario.setNome(rs.getString("nome"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setBairro(rs.getString("bairro"));
        usuario.setLogradouro(rs.getString("logradouro"));
        usuario.setNumero(rs.getLong("numero"));
        usuario.setDataDeNascimento(rs.getDate("data_nascimento"));
        usuario.setIdade(rs.getInt("idade"));

        funcionario.setEmail(rs.getString("email"));
        funcionario.setNome(rs.getString("nome"));
        funcionario.setTelefone(rs.getString("telefone"));
        funcionario.setBairro(rs.getString("bairro"));
        funcionario.setLogradouro(rs.getString("logradouro"));
        funcionario.setNumero(rs.getLong("numero"));
        funcionario.setDataDeNascimento(rs.getDate("data_nascimento"));
        funcionario.setIdade(rs.getInt("idade"));
        funcionario.setCargo(rs.getString("carto"));

        funcionario.setUsuario(usuario);

        return funcionario;
    }
}
