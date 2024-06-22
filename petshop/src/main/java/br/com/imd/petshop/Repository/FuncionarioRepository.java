package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.DTO.ClienteDTO;
import br.com.imd.petshop.DTO.FuncionarioDTO;
import br.com.imd.petshop.Entity.Funcionario;
import br.com.imd.petshop.Entity.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FuncionarioRepository {
    public void save(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (usuario_email, cargo) VALUES (?, ?)";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, funcionario.getUsuario().getEmail());
            ps.setString(2, funcionario.getCargo());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Funcionario findByUsuario(String usuario_email) {
        String sql = "SELECT * FROM funcionario WHERE usuario_email = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario_email);
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
        usuario.setEmail(rs.getString("usuario_email"));
        funcionario.setUsuario(usuario);
        funcionario.setCargo(rs.getString("cargo"));
        return funcionario;
    }

    public List<FuncionarioDTO> listarFuncionariosComUsuarios() {
        String sql = "SELECT f.usuario_email, f.cargo, u.nome, u.telefone, u.data_nascimento FROM funcionario f INNER JOIN usuario u ON f.usuario_email = u.email";
        List<FuncionarioDTO> funcionarios = new ArrayList<>();
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
                funcionarioDTO.setEmail(rs.getString("usuario_email"));
                funcionarioDTO.setCargo(rs.getString("cargo"));
                funcionarioDTO.setNome(rs.getString("nome"));
                funcionarioDTO.setTelefone(rs.getString("telefone"));
                funcionarioDTO.setDataNascimento(rs.getDate("data_nascimento"));
                funcionarios.add(funcionarioDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }

}
