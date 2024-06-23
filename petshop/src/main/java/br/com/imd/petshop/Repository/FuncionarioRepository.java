package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.DTO.ClienteDTO;
import br.com.imd.petshop.DTO.FuncionarioDTO;
import br.com.imd.petshop.DTO.UsuarioDTO;
import br.com.imd.petshop.Entity.Cep;
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

    public FuncionarioDTO findFuncionarioDTOByEmail(String email) {
        String sql = "SELECT f.cargo, u.nome, u.telefone, u.data_nascimento, u.logradouro, u.numero, u.bairro, u.cep, c.cidade, c.estado " +
                "FROM funcionario f " +
                "INNER JOIN usuario u ON f.usuario_email = u.email " +
                "LEFT JOIN cep c ON u.cep = c.cep " +
                "WHERE u.email = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
                    funcionarioDTO.setEmail(email);
                    funcionarioDTO.setCargo(rs.getString("cargo"));
                    funcionarioDTO.setNome(rs.getString("nome"));
                    funcionarioDTO.setTelefone(rs.getString("telefone"));
                    funcionarioDTO.setDataNascimento(rs.getDate("data_nascimento"));
                    funcionarioDTO.setLogradouro(rs.getString("logradouro"));
                    funcionarioDTO.setNumero(rs.getLong("numero"));
                    funcionarioDTO.setBairro(rs.getString("bairro"));

                    Cep cep = new Cep();
                    cep.setCep(rs.getString("cep"));
                    cep.setCidade(rs.getString("cidade"));
                    cep.setEstado(rs.getString("estado"));

                    funcionarioDTO.setCep(cep);
                    return funcionarioDTO;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void atualizarFuncionario(UsuarioDTO usuarioDTO) {
        String updateUsuarioSql = "UPDATE usuario SET nome = ?, telefone = ?, logradouro = ?, numero = ?, bairro = ? WHERE email = ?";
        String updateCepUsuarioSql = "UPDATE usuario SET cep = ? WHERE email = ?";
        String updateCepSql = "UPDATE cep SET cidade = ?, estado = ? WHERE cep = ?";
        String updateFuncionarioSql = "UPDATE funcionario SET cargo = ? WHERE usuario_email = ?";

        try (Connection conn = DataBaseConfig.getConnection()) {
            // Atualiza os dados do usuário na tabela usuario
            try (PreparedStatement ps = conn.prepareStatement(updateUsuarioSql)) {
                ps.setString(1, usuarioDTO.getNome());
                ps.setString(2, usuarioDTO.getTelefone());
                ps.setString(3, usuarioDTO.getLogradouro());
                ps.setLong(4, usuarioDTO.getNumero());
                ps.setString(5, usuarioDTO.getBairro());
                ps.setString(6, usuarioDTO.getEmail());
                ps.executeUpdate();
            }

            // Atualiza o cep do usuário na tabela usuario
            try (PreparedStatement ps = conn.prepareStatement(updateCepUsuarioSql)) {
                ps.setString(1, usuarioDTO.getCep().getCep());
                ps.setString(2, usuarioDTO.getEmail());
                ps.executeUpdate();
            }

            // Atualiza o cep na tabela cep
            try (PreparedStatement ps = conn.prepareStatement(updateCepSql)) {
                ps.setString(1, usuarioDTO.getCep().getCidade());
                ps.setString(2, usuarioDTO.getCep().getEstado());
                ps.setString(3, usuarioDTO.getCep().getCep());
                ps.executeUpdate();
            }

            // Atualiza o cargo do funcionário
            try (PreparedStatement ps = conn.prepareStatement(updateFuncionarioSql)) {
                ps.setString(1, usuarioDTO.getCargo());
                ps.setString(2, usuarioDTO.getEmail());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }



}
