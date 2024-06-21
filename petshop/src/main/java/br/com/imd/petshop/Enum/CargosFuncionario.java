package br.com.imd.petshop.Enum;

public enum CargosFuncionario {
    VETERINARIO("Veterinário"),
    AUXILIAR_VETERINARIO("Auxiliar de Veterinário"),
    RECEPCIONISTA("Recepcionista"),
    GROOMER("Groomer"),
    GERENTE("Gerente");

    private final String descricao;

    CargosFuncionario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
