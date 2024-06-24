package br.com.imd.petshop.DTO;

import java.util.Date;
import java.util.List;

public class PedidoDTO {

    private Double valor;

    private Date data;

    private String status;

    private String funcionarioId;

    private String clienteId;

    private List<CarrinhoDTO> carrinhoDTO;

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(String funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<CarrinhoDTO> getCarrinhoDTO() {
        return carrinhoDTO;
    }

    public void setCarrinhoDTO(List<CarrinhoDTO> carrinhoDTO) {
        this.carrinhoDTO = carrinhoDTO;
    }
}
