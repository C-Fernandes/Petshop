package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Entity.Cep;
import br.com.imd.petshop.Config.DataBaseConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class CepRepository {

    public boolean existsById(String cep) {
        String sql = "SELECT COUNT(*) AS count FROM cep WHERE cep = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cep);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Cep> findById(String cep) {
        String sql = "SELECT * FROM cep WHERE cep = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cep);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void save(Cep cep) {
        String sql = "INSERT INTO cep (cep, cidade, estado) VALUES (?, ?, ?)";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cep.getCep());
            ps.setString(2, cep.getCidade());
            ps.setString(3, cep.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Cep mapRow(ResultSet rs) throws SQLException {
        Cep cep = new Cep();
        cep.setCep(rs.getString("cep"));
        cep.setCidade(rs.getString("cidade"));
        cep.setEstado(rs.getString("estado"));
        return cep;
    }
}
