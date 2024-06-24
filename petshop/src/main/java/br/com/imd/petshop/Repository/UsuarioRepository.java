package br.com.imd.petshop.Repository;

import br.com.imd.petshop.DTO.UsuarioDTO;
import br.com.imd.petshop.Entity.Cep;
import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Config.DataBaseConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioRepository {

    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuario (email, senha, nome, data_nascimento, idade, telefone, logradouro, numero, bairro, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getNome());
            ps.setDate(4, new java.sql.Date(usuario.getDataDeNascimento().getTime()));
            ps.setInt(5, usuario.getIdade());
            ps.setString(6, usuario.getTelefone());
            ps.setString(7, usuario.getLogradouro());
            ps.setLong(8, usuario.getNumero());
            ps.setString(9, usuario.getBairro());
            ps.setString(10, usuario.getCep().getCep());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = mapRow(rs);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public Usuario findByEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
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

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setNome(rs.getString("nome"));
        usuario.setDataDeNascimento(rs.getDate("data_nascimento"));
        usuario.setIdade(rs.getInt("idade"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setLogradouro(rs.getString("logradouro"));
        usuario.setNumero(rs.getLong("numero"));
        usuario.setBairro(rs.getString("bairro"));
        return usuario;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) AS count FROM usuario WHERE email = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void desativarUsuario(String email) {
        String sqlDesativarUsuario = "UPDATE usuario SET active = false WHERE email = ?";
        String sqlDesativarPet = "UPDATE pet SET active = false WHERE cliente_usuario_email = ?";
        String sqlDesativarSolicitacao = "UPDATE solicita SET active = false WHERE cliente_usuario_email = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement psDesativarUsuario = conn.prepareStatement(sqlDesativarUsuario);
             PreparedStatement psDesativarPet = conn.prepareStatement(sqlDesativarPet);
             PreparedStatement psDesativarSolicitacao = conn.prepareStatement(sqlDesativarSolicitacao)) {

            conn.setAutoCommit(false);

            psDesativarUsuario.setString(1, email);
            psDesativarUsuario.executeUpdate();

            psDesativarPet.setString(1, email);
            psDesativarPet.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UsuarioDTO> listarClientes() {
        String sql = "SELECT u.*, c.qtd_pontos " +
                "FROM usuario u " +
                "INNER JOIN cliente c ON u.email = c.usuario_email " +
                "WHERE u.active = true";
        return listarUsuariosPorQuery(sql);
    }

    public List<UsuarioDTO> listarFuncionarios() {
        String sql = "SELECT u.*, f.cargo " +
                "FROM usuario u " +
                "INNER JOIN funcionario f ON u.email = f.usuario_email " +
                "WHERE u.active = true";
        return listarUsuariosPorQuery(sql);
    }

    private List<UsuarioDTO> listarUsuariosPorQuery(String sql) {
        List<UsuarioDTO> usuarios = new ArrayList<>();
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UsuarioDTO usuario = mapRowToDTO(rs);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }


    private UsuarioDTO mapRowToDTO(ResultSet rs) throws SQLException {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setNome(rs.getString("nome"));
        usuario.setDataDeNascimento(rs.getDate("data_nascimento"));
        usuario.setIdade(rs.getInt("idade"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setLogradouro(rs.getString("logradouro"));
        usuario.setNumero(rs.getLong("numero"));
        usuario.setBairro(rs.getString("bairro"));
        usuario.setCargo(rs.getString("cargo"));

        return usuario;
    }


    public Usuario findByEmailTelaDados(String email) throws SQLException {
        String sql = "SELECT u.nome, u.email, u.data_nascimento, u.telefone, u.numero, u.logradouro, u.bairro, " +
                "c.cep AS c_cep, c.cidade, c.estado " +  // Adicionado espaço após estado
                "FROM usuario u " +
                "JOIN cep c ON u.cep = c.cep " +
                "WHERE u.email = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setDataDeNascimento(rs.getDate("data_nascimento"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setLogradouro(rs.getString("logradouro"));
                usuario.setNumero(rs.getLong("numero"));
                usuario.setBairro(rs.getString("bairro"));

                Cep cep = new Cep();
                cep.setCep(rs.getString("c_cep"));
                cep.setCidade(rs.getString("cidade"));
                cep.setEstado(rs.getString("estado"));

                usuario.setCep(cep);

                return usuario;
            }
        }
        return null; // Usuario não encontrado
    }




}
