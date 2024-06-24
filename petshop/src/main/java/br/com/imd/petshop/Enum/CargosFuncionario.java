package br.com.imd.petshop.Enum;

public enum CargosFuncionario {
    VETERINARIO("Veterinário"),
    RECEPCIONISTA("Recepcionista"),
    GERENTE("Gerente"),;

    private final String descricao;

    CargosFuncionario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
