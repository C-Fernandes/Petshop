package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.DTO.ClienteDTO;
import br.com.imd.petshop.Entity.Cliente;
import br.com.imd.petshop.Entity.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteRepository {

    public void save(Cliente cliente) {
        String sql = "INSERT INTO cliente (usuario_email, qtd_pontos) VALUES (?, ?)";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getUsuario().getEmail());
            ps.setLong(2, cliente.getQtdPontos());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente findByUsuario(String usuario_email) {
        String sql = "SELECT * FROM cliente WHERE usuario_email = ?";
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

    public Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        Usuario usuario = new Usuario();
        usuario.setEmail(rs.getString("usuario_email"));
        cliente.setUsuario(usuario);
        cliente.setQtdPontos(rs.getLong("qtd_pontos"));
        return cliente;
    }

    public List<ClienteDTO> listarClientesComUsuarios() {
        String sql = "SELECT c.usuario_email, c.qtd_pontos, u.nome, u.telefone, u.data_nascimento FROM cliente c INNER JOIN usuario u ON c.usuario_email = u.email";
        List<ClienteDTO> clientes = new ArrayList<>();
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClienteDTO clienteDTO = new ClienteDTO();
                clienteDTO.setEmail(rs.getString("usuario_email"));
                clienteDTO.setQtdPontos(rs.getLong("qtd_pontos"));
                clienteDTO.setNome(rs.getString("nome"));
                clienteDTO.setTelefone(rs.getString("telefone"));
                clienteDTO.setDataNascimento(rs.getDate("data_nascimento"));
                clientes.add(clienteDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

}
