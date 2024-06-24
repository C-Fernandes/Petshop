package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;
import br.com.imd.petshop.Entity.Servico;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServicoRepository {

    public Servico save(Servico servico) {
        String sql = "INSERT INTO servico (descricao, nome, pontos_servico) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, servico.getDescricao());
            ps.setString(2, servico.getNome());
            ps.setInt(3, servico.getPontosServico());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("A inserção do serviço falhou, nenhuma linha foi afetada.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    servico.setId(generatedKeys.getLong(1));
                    return servico;
                } else {
                    throw new SQLException("A inserção do serviço falhou, nenhum ID foi gerado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveProdutoAndPreco(Servico servico, Preco preco) {
        String sql = "INSERT INTO servico_has_preco (servico_id, preco_id) VALUES (?, ?)";
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, servico.getId());
            ps.setLong(2, preco.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Servico> findAll() {
        String sql = "SELECT * FROM servico";
        List<Servico> servicos = new ArrayList<>();
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                servicos.add(mapRow(rs));
            }

            return servicos;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM servico WHERE id = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Servico mapRow(ResultSet rs) throws SQLException {
        Servico servico = new Servico();
        servico.setId(rs.getLong("id"));
        servico.setDescricao(rs.getString("descricao"));
        servico.setNome(rs.getString("nome"));
        servico.setPontosServico(rs.getInt("pontos_servico"));
        return servico;
    }
}