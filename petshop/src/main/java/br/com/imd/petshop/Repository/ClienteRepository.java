package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.DTO.ClienteDTO;
import br.com.imd.petshop.DTO.FuncionarioDTO;
import br.com.imd.petshop.DTO.UsuarioDTO;
import br.com.imd.petshop.Entity.Cep;
import br.com.imd.petshop.Entity.Cliente;
import br.com.imd.petshop.Entity.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
        usuario.setEmail(rs.getString("usuario_email"));
        cliente.setUsuario(usuario);
        cliente.setQtdPontos(rs.getLong("qtd_pontos"));
        return cliente;
    }


    public List<ClienteDTO> listarClientesComUsuarios() {
        String sql = "SELECT c.usuario_email, c.qtd_pontos, u.nome, u.telefone, u.data_nascimento FROM cliente c INNER JOIN usuario u ON c.usuario_email = u.email WHERE u.active = true";
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

    public ClienteDTO findClienteDTOByEmail(String email) {
        String sql = "SELECT u.nome, u.telefone, u.data_nascimento, u.logradouro, u.numero, u.bairro, u.cep, c.cidade, c.estado " +
                "FROM cliente l " +
                "INNER JOIN usuario u ON l.usuario_email = u.email " +
                "LEFT JOIN cep c ON u.cep = c.cep " +
                "WHERE u.email = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClienteDTO clienteDTO = new ClienteDTO();
                    clienteDTO.setEmail(email);
                    clienteDTO.setNome(rs.getString("nome"));
                    clienteDTO.setTelefone(rs.getString("telefone"));
                    clienteDTO.setDataNascimento(rs.getDate("data_nascimento"));
                    clienteDTO.setLogradouro(rs.getString("logradouro"));
                    clienteDTO.setNumero(rs.getLong("numero"));
                    clienteDTO.setBairro(rs.getString("bairro"));

                    Cep cep = new Cep();
                    cep.setCep(rs.getString("cep"));
                    cep.setCidade(rs.getString("cidade"));
                    cep.setEstado(rs.getString("estado"));

                    clienteDTO.setCep(cep);
                    return clienteDTO;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public void atualizarCliente(UsuarioDTO usuarioDTO) {
        String updateUsuarioSql = "UPDATE usuario SET nome = ?, telefone = ?, logradouro = ?, numero = ?, bairro = ? WHERE email = ?";
        String updateCepUsuarioSql = "UPDATE usuario SET cep = ? WHERE email = ?";
        String updateCepSql = "UPDATE cep SET cidade = ?, estado = ? WHERE cep = ?";

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
        } catch (SQLException e) {
            throw new IllegalArgumentException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

}
