package br.com.imd.petshop.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.imd.petshop.Config.DataBaseConfig;
import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Produto;

@Repository
public class ProdutoRepository {

    private Produto mapeamento(ResultSet resultSet) throws SQLException {
        Produto produto = new Produto();
        produto.setId(resultSet.getLong("id"));
        produto.setNome(resultSet.getString("nome"));
        produto.setQuantidade(Integer.parseInt(resultSet.getString("quantidade")));
        produto.setAtivo(resultSet.getBoolean("ativo"));

        return produto;
    }

    public void deleteProduto(Long idProduto) {
        String deleteProdutoSql = "DELETE FROM produto WHERE id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(deleteProdutoSql)) {
            ps.setLong(1, idProduto);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Nenhum produto foi deletado. Produto não encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Produto saveProdutoEPreco(Produto produto) {
        String sql = "INSERT INTO produto (nome, quantidade, ativo) VALUES (?, ?, ?)";
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidade());
            ps.setBoolean(3, produto.getAtivo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("A inserção do produto falhou, nenhuma linha foi afetada.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getLong(1));
                    return produto;
                } else {
                    throw new SQLException("A inserção do produto falhou, nenhum ID foi gerado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void salvarRelacionamento(Produto produto, Preco preco) {
        String sql = "INSERT INTO produto_has_preco (produto_id, preco_id) VALUES (?, ?)";
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, produto.getId());
            ps.setLong(2, preco.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> findAll() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                produtos.add(mapeamento(rs));
            }

            return produtos;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Produto findbyId(Long id) {
        Produto p = new Produto();
        String sql = "SELECT * FROM produto WHERE id = ?";

        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Produto prod = (mapeamento(rs));
                p.setAtivo(prod.getAtivo());
                p.setId(prod.getId());
                p.setNome(prod.getNome());
                p.setQuantidade(prod.getQuantidade());
                p.setPreco(prod.getPreco());
            }

            return p;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void atualizarProduto(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, quantidade = ?, ativo = ? WHERE id = ?";
        try {
            Connection conn = DataBaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidade());
            ps.setBoolean(3, produto.getAtivo());
            ps.setLong(4, produto.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Falha ao atualizar o produto. Nenhum registro foi modificado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
