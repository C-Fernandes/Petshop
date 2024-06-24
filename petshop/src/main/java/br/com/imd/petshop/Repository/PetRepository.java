package br.com.imd.petshop.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.imd.petshop.Entity.Cliente;
import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Entity.Raca;
import br.com.imd.petshop.Config.DataBaseConfig;

@Repository
public class PetRepository {

    private Pet mapeamento(ResultSet resultSet) throws SQLException {
        Pet pet = new Pet();
        pet.setId(resultSet.getLong("id"));
        pet.setNome(resultSet.getString("nome"));
        pet.setDataDeNascimento(resultSet.getDate("data_de_nascimento"));
        pet.setImagem(resultSet.getString("imagem"));
        pet.setActive(resultSet.getBoolean("ativo"));
        pet.setSexo(resultSet.getString("sexo").charAt(0));
        pet.setPeso(resultSet.getDouble("peso"));

        Cliente dono = new Cliente();
        Raca raca = new Raca();
        raca.setRaca(resultSet.getString("raca"));
        dono.setEmail(resultSet.getString("cliente_usuario_email"));

        pet.setDono(dono);
        pet.setRaca(raca);

        return pet;
    }

    public List<Pet> findByNomeContainingIgnoreCase(String nome, String email) {
        String sql = "SELECT * FROM pet WHERE LOWER(nome) LIKE LOWER(?) AND cliente_usuario_email = ? AND ativo = true";
        List<Pet> pets = new ArrayList<>();
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pets.add(mapeamento(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    public void atualizarPet(Pet pet) {
        String sql = "UPDATE pet SET nome = ?, peso = ?, imagem = ? WHERE id = ?";

        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pet.getNome());
            statement.setDouble(2, pet.getPeso());
            statement.setString(3, pet.getImagem());
            statement.setLong(4, pet.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserirPet(Pet pet) {
        String sql = "INSERT INTO pet (nome, data_de_nascimento, cliente_usuario_email, raca, imagem, ativo, sexo, peso) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pet.getNome());
            statement.setDate(2, new java.sql.Date(pet.getDataDeNascimento().getTime()));
            statement.setString(3, pet.getDono().getEmail());
            statement.setString(4, pet.getRaca().getRaca());
            statement.setString(5, pet.getImagem());
            statement.setBoolean(6, true); // Sempre ativo ao inserir
            statement.setString(7, String.valueOf(pet.getSexo())); // Converte char para String
            statement.setDouble(8, pet.getPeso());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pet> listarPets(String clienteEmail) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pet WHERE cliente_usuario_email = ? AND ativo = 1";

        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, clienteEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pets.add(mapeamento(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    public List<Pet> procurarPorNome(String name) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pet WHERE nome = ? AND ativo = 1";

        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pets.add(mapeamento(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    public List<Pet> findByUser(String emailUser) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pet WHERE cliente_usuario_email = ? AND ativo = 1";

        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, emailUser);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pets.add(mapeamento(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    public Pet procurarPorId(Long id) {
        String sql = "SELECT * FROM pet WHERE id = ? AND ativo = true";

        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapeamento(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void desativarPet(Long id) {
        String sql = "UPDATE pet SET ativo = false WHERE id = ?";

        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
