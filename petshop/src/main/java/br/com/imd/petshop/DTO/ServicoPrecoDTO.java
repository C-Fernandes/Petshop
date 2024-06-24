package br.com.imd.petshop.DTO;

import br.com.imd.petshop.Entity.Preco;
import br.com.imd.petshop.Entity.Servico;

public class ServicoPrecoDTO {
    private Servico servico;
    private Preco preco;

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Preco getPreco() {
        return preco;
    }

    public void setPreco(Preco preco) {
        this.preco = preco;
    }
}
