package br.com.imd.petshop.Repository;

import java.util.List;
import java.util.ArrayList;

import java.sql.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.imd.petshop.Entity.Pet;
import br.com.imd.petshop.Entity.Usuario;
import br.com.imd.petshop.Entity.Cep;
import br.com.imd.petshop.Entity.Cliente;

public class PetRepository {

    @Autowired
    private DataSource dataSource;

    public void inserirPet(Pet pet) {
        String sql = "INSERT INTO pet (nome, data_de_nascimento, usuario_email, raca_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, pet.getNome());
            statement.setDate(2, new java.sql.Date(pet.getDataDeNascimento().getTime()));
            statement.setString(3, pet.getDono().getUsuario().getEmail());
            statement.setLong(4, pet.getRaca());

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

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
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
                cep.setCep(resultSet.getLong("cep"));
                cep.setCidade(resultSet.getString("cidade"));
                cep.setEstado(resultSet.getString("estado"));

                Cliente cliente = new Cliente();
                cliente.setUsuario(usuario);
                cliente.setQtdPontos(resultSet.getLong("qtd_pontos"));

                pet.setDono(cliente);

                pets.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

}
