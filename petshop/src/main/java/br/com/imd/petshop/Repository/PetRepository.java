package br.com.imd.petshop.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.Cep;

@Repository
public class PetRepository {

    private Pet mapeamento(ResultSet resultSet) throws SQLException {
        Pet pet = new Pet();
        pet.setId(resultSet.getLong("u.email"));
        pet.setNome(resultSet.getString("nome"));
        pet.setDataDeNascimento(resultSet.getDate("data_de_nascimento"));

        Usuario usuario = new Usuario();
        usuario.setEmail(resultSet.getString("u.email"));
        usuario.setNome(resultSet.getString("u.nome"));
        usuario.setDataDeNascimento(resultSet.getDate("u.data_de_nascimento"));
        usuario.setTelefone(resultSet.getString("u.telefone"));
        usuario.setLogradouro(resultSet.getString("u.logradouro"));
        usuario.setNumero(resultSet.getLong("u.numero"));
        usuario.setBairro(resultSet.getString("u.bairro"));

        Cep cep = new Cep();
        cep.setCep(resultSet.getString("cep"));
        cep.setCidade(resultSet.getString("cidade"));
        cep.setEstado(resultSet.getString("estado"));

        pet.setDono(usuario);
        return pet;

    }

    public void inserirPet(Pet pet) {
        String sql = "INSERT INTO pet (nome, data_de_nascimento, cliente_usuario_email, raca_especie) VALUES (?, ?, ?, ?)";

        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, pet.getNome());
            statement.setDate(2, new java.sql.Date(pet.getDataDeNascimento().getTime()));
            statement.setString(3, pet.getDono().getEmail());
            statement.setString(4, pet.getRaca().getRaca());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                pet.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Falha ao inserir pet, nenhum ID gerado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pet> listarPets() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pet p JOIN cliente c ON cliente_email = email NATURAL JOIN usuario NATURAL JOIN cep";

        try (Connection connection = DataBaseConfig.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {

                pets.add(mapeamento(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    public List<Pet> procurarPorNome(String name) {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM pets WHERE nome = ?")) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                pets.add(mapeamento(resultSet));
            }
            return pets;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public Pet procurarPorId(Long id) {
        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM pets WHERE id = ?")) {

            statement.setLong(1, id);
            return mapeamento(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public void deletar(Long id) {
        try (Connection connection = DataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM pets WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}
