package br.com.imd.petshop.Repository;

import br.com.imd.petshop.Entity.Raca;
import br.com.imd.petshop.Config.DataBaseConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RacaRepository {

    public boolean existsById(String raca) {
        String sql = "SELECT COUNT(*) AS count FROM raca WHERE raca = ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, raca);
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

    public Optional<Raca> findById(String raca) {
        String sql = "SELECT * FROM raca WHERE raca = ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, raca);
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

    public Raca findByEspecie(String especie) {
        Raca raca = new Raca();
        String sql = "SELECT * FROM raca WHERE especie = ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, especie);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    raca = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return raca;
    }

    public List<Raca> findAll() {
        List<Raca> racas = new ArrayList<>();
        String sql = "SELECT * FROM raca";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                racas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return racas;
    }

    public void save(Raca raca) {
        String sql = "INSERT INTO raca (raca, especie) VALUES (?, ?)";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, raca.getRaca());
            ps.setString(2, raca.getEspecie());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Raca raca) {
        String sql = "UPDATE raca SET especie = ? WHERE raca = ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, raca.getEspecie());
            ps.setString(2, raca.getRaca());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String raca) {
        String sql = "DELETE FROM raca WHERE raca = ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, raca);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Raca mapRow(ResultSet rs) throws SQLException {
        Raca raca = new Raca();
        raca.setRaca(rs.getString("raca"));
        raca.setEspecie(rs.getString("especie"));
        return raca;
    }
}
