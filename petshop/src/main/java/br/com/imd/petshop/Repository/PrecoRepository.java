package br.com.imd.petshop.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.Preco;

@Repository
public class PrecoRepository {
    private Preco mapeamento(ResultSet resultSet) throws SQLException {
        Preco preco = new Preco();
        preco.setId(resultSet.getLong("id"));
        preco.setValor(resultSet.getDouble("valor"));
        preco.setData(resultSet.getTimestamp("data"));

        return preco;
    }

    public Preco save(Preco preco) {
        String sql = "INSERT INTO Preco (data, valor) VALUES (?, ?)";
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(1, currentTimestamp);
            ps.setDouble(2, preco.getValor());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    preco.setId(generatedKeys.getLong(1));
                    preco.setData(currentTimestamp);
                    return preco;
                } else {
                    throw new SQLException("A inserção do preço falhou, nenhum ID foi gerado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Preco findbyId(Long id) {

        String sql = "SELECT * FROM preco WHERE id = ?";
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return mapeamento(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Preco findbyDateEValor(Date localDate, Double valor) {
        String sql = "SELECT * FROM preco WHERE data = ? AND valor = ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, localDate);
            ps.setDouble(2, valor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeamento(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Preco> findByProduto(Long id) {
        List<Preco> precos = new ArrayList<>();
        List<Long> idPrecos = new ArrayList<>();
        String sql = "SELECT * FROM produto_has_preco WHERE produto_id = ?";
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idPrecos.add(rs.getLong("preco_id"));
            }
            String sqlAtt = "SELECT * FROM preco WHERE id = ?";
            for (Long ids : idPrecos) {
                ps = conn.prepareStatement(sqlAtt);
                ps.setLong(1, ids);
                rs = ps.executeQuery();
                while (rs.next()) {
                    precos.add(mapeamento(rs));
                }
            }
            return precos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verificarProdutoHasPreco(Long idPreco) {
        String sql = "SELECT COUNT(*) AS total FROM produto_has_preco WHERE preco_id = ?";

        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idPreco);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    if (total > 0) {
                        return true;
                    }
                    // Retorna true se o preço tiver pelo menos um produto associado
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Retorna false por padrão em caso de erro ou nenhum preço associado ao produto
    }

    public void deletarPreco(Long idPreco) {
        String sql = "DELETE FROM preco WHERE id = ?";

        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idPreco);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Preço deletado com sucesso.");
            } else {
                System.out.println("Nenhum preço foi deletado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
