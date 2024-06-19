package br.com.imd.petshop.Repository;

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
}
