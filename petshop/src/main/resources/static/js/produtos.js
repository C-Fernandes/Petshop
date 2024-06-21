var idProduto = 0,
    date = "",
    idPre = 0, nome = "", quantidade = 0, preco = 0;
var stompClient = null;

function connect() {
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("Connected: " + frame);
        stompClient.subscribe("/topic/products", function (message) {
            console.log("Product update message received");

        });
    });
}

function fetchProducts() {
    fetch("/produtos/listar")
        .then((response) => response.json())
        .then((data) => {
            var productContainer = document.getElementById("product-container");
            productContainer.innerHTML = "";
            data.forEach((produto) => {
                var card = document.createElement("div");
                card.className = "card div-editar";
                card.setAttribute("data-id", produto.id);
                card.setAttribute("data-nome", produto.nome);
                card.setAttribute("data-quantidade", produto.quantidade);
                card.setAttribute("data-ativo", produto.ativo);
                card.setAttribute("data-preco", produto.preco.valor);
                card.setAttribute("data-id-preco", produto.preco.id);
                card.setAttribute("data-data-preco", produto.preco.data);
                card.innerHTML = `
                            <div class="card-body">
                                <h5 class="card-title">${produto.nome}</h5>
                                <div class="card-text">
                                    <p>Quantidade: ${produto.quantidade}</p>
                                    <p>Valor: R$ ${produto.preco.valor}</p>
                                </div>
                            </div>`;
                productContainer.appendChild(card);

            });
        })
        .catch((error) => console.error("Error fetching products:", error));
}
$(document).ready(function () {
    connect();

    $(document).on('click', '.div-editar', function () {
        idProduto = $(this).data("id");
        nome = $(this).data("nome");
        quantidade = $(this).data("quantidade");
        var ativo = $(this).data("ativo");
        var preco = $(this).data("preco");
        idPre = $(this).data("id-preco");
        date = $(this).data("data-preco");




        $("#modalAtualizacao")
            .find(".modal-title")
            .text("Editar: " + nome);

        $("#nomeProdutoAtualizacao").val(nome);
        $("#ativoProdutoAtualizacao").prop("checked", ativo);
        $("#quantidadeProdutoAtualizacao").val(quantidade);
        $("#precoProdutoAtualizacao").val(preco);

        $("#modalAtualizacao").modal("show");
    });

    $("#atualizarButton").click(function () {
        console.log(idPre);
        const nome = $("#nomeProdutoAtualizacao").val();
        const quantidade = $("#quantidadeProdutoAtualizacao").val();
        const ativo = $("#ativoProdutoAtualizacao").prop("checked");
        const preco = parseFloat($("#precoProdutoAtualizacao").val());

        const produtoPrecoDTO = {
            produto: {
                id: idProduto,
                nome: nome,
                quantidade: quantidade,
                ativo: ativo,
            },
            preco: { id: idPre, valor: preco, data: date },
        };
        console.log("Entrou aqui");
        fetch("/produtos/atualizar", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(produtoPrecoDTO),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Erro ao atualizar produto.");
                }
                return response; // Retorna a resposta inteira, sem interpretar como JSON
            })
            .then(() => {
                console.log("Produto atualizado com sucesso.");
                $("#modalAtualizacao").modal("hide"); // Esconde o modal de atualização
            })
            .catch((error) => {
                console.error("Erro ao atualizar produto:", error); // Exibe o erro no console
                // Aqui você pode exibir uma mensagem de erro na interface, se desejar
            });
        fetchProducts();
    });

    $("#nomeProduto, #quantidadeProduto, #precoProduto").keyup(
        verificarCampos
    );

    $("#exampleModal").on("show.bs.modal", function (event) {
        var button = $(event.relatedTarget);
        var recipient = button.data("whatever");
        var modal = $(this);
        modal.find(".modal-title").text("Cadastrar produto");
        modal.find(".modal-body input").val(recipient);
    });

    function verificarCampos() {
        var nome = $("#nomeProdutoCadastro").val();
        var quantidade = $("#quantidadeProdutoCadastro").val();
        var preco = $("#precoProdutoCadastro").val();

        if (nome !== "" && quantidade !== "" && preco !== "") {
            $("#cadastrarButton").prop("disabled", false);
            $("#erroCadastro").hide();
        } else {
            $("#cadastrarButton").prop("disabled", true);
            $("#erroCadastro").show();
        }
    }

    $(
        "#nomeProdutoCadastro, #quantidadeProdutoCadastro, #precoProdutoCadastro"
    ).keyup(verificarCampos);




    $("#deleteButton").click(function () {

        fetch(`/produtos/${idProduto}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Erro ao deletar produto.");
                }
                fetchProducts();
                return response;
            })
            .then((data) => {
                console.log("Produto deletado com sucesso:", data);
                // Ocultar o modal ou executar outra ação após a exclusão
                $("#exampleModal").modal("hide");
            })
            .catch((error) => {
                console.error("Erro ao deletar produto:", error);
            });
    });


    $("#deleteButtonModal").click(function () {


        $("#modalAtualizacao").modal("hide");
        $("#modalDelete").modal("show");
        console.log(nome);

        $("#modalDelete")
            .find(".modal-title")
            .text("Deletar: " + nome);
    });
    $("#cadastrarButton").click(function () {
        console.log("Entrou na função");
        verificarCampos();

        const nome = $("#nomeProdutoCadastro").val();
        const quantidade = $("#quantidadeProdutoCadastro").val();
        const ativo = $("#ativoProduto").prop("checked");
        const preco = parseFloat($("#precoProdutoCadastro").val());

        const produtoPrecoDTO = {
            produto: { nome, quantidade, ativo },
            preco: { valor: preco },
        };

        fetch("/produtos/cadastro-produto", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(produtoPrecoDTO),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Erro ao cadastrar produto e preço.");
                } fetchProducts();
                return response.json();
            })
            .then((data) => {
                console.log("Produto e Preço cadastrados com sucesso:", data);
                $("#exampleModal").modal("hide");
            })
            .catch((error) => {
                console.error("Erro ao cadastrar produto e preço:", error);
            });

    });
});