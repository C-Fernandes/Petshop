var idProduto = 0,
    date = "",
    idPre = 0, nome = "", quantidade = 0, preco = 0, imagem;
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
} function fetchProducts() {
    fetch("/produtos/listar", {
        method: "GET"
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error("Erro ao buscar produtos.");
            }
            return response.json();
        })
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
                card.setAttribute("data-imagem", produto.imagem);
                var img = "/images/uploads/produtos/" + produto.imagem;
                console.log("Imagem: " + img);
                card.innerHTML = `

                <img src="${img}" alt="Imagem do Produto" accept="image/*" />
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
        .catch((error) => console.error("Erro ao buscar produtos:", error));
}

// Função para verificar se a imagem está disponível
function checkImageAvailability(url, callback) {
    fetch(url, { method: 'HEAD' })
        .then((response) => {
            if (response.ok) {
                callback(true);
            } else {
                setTimeout(() => checkImageAvailability(url, callback), 100); // Tenta novamente após 1 segundo
            }
        })
        .catch((error) => {
            console.error('Erro ao verificar disponibilidade da imagem:', error);
            setTimeout(() => checkImageAvailability(url, callback), 1000); // Tenta novamente após 1 segundo
        });
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

        imagem = $(this).data("imagem");
        $("#modalAtualizacao")
            .find(".modal-title")
            .text("Editar: " + nome);

        $("#nomeProdutoAtualizacao").val(nome);
        $("#ativoProdutoAtualizacao").prop("checked", ativo);
        $("#quantidadeProdutoAtualizacao").val(quantidade);
        $("#precoProdutoAtualizacao").val(preco);

        $("#modalAtualizacao").modal("show");
    });
    $("#buscar").click(function () {
        const filtroNome = $("#filtroNome").val().trim();
        if (filtroNome !== "") {
            // Filtrar produtos pelo nome
            fetch(`/produtos/buscar?nome=${filtroNome}`)
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Erro ao buscar produtos.");
                    }
                    return response.json();
                })
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
                        card.setAttribute("data-imagem", produto.imagem);
                        var img = "/images/uploads/produtos/" + produto.imagem;
                        console.log("Imagem: " + img);
                        card.innerHTML = `
                            <img src="${img}" alt="Imagem do Produto" accept="image/*" />
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
                .catch((error) => console.error("Erro ao buscar produtos:", error));
        } else {
            // Se o campo de filtro estiver vazio, buscar todos os produtos
            fetchProducts();
        }
    });
    $("#atualizarButton").click(function () {
        const nome = $("#nomeProdutoAtualizacao").val();
        const quantidade = $("#quantidadeProdutoAtualizacao").val();
        const ativo = $("#ativoProdutoAtualizacao").prop("checked");
        const preco = parseFloat($("#precoProdutoAtualizacao").val());
        const formData = new FormData();

        // Verifica se o elemento existe e se foi selecionado um arquivo
        if ($("#imgProdutoAtualizacao")[0] && $("#imgProdutoAtualizacao")[0].files[0]) {
            formData.append("file", $("#imgProdutoAtualizacao")[0].files[0]);
        }
        formData.append("produtoDTO", JSON.stringify({ produto: { id: idProduto, nome: nome, quantidade: quantidade, ativo: ativo }, preco: { id: idPre, valor: preco, data: date } }));


        fetch("/produtos/atualizar", {
            method: "PUT",
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        if (data.errors) {
                            data.errors.forEach(error => {
                                switch (error) {
                                    case "O nome do produto é obrigatório.":
                                        document.getElementById("mensagemNome").innerText = error;
                                        break;
                                    case "A quantidade é obrigatória.":
                                    case "A quantidade deve ser maior que zero.":
                                    case "A quantidade deve ser um número inteiro positivo.":
                                        document.getElementById("mensagemQuantidade").innerText = error;
                                        break;
                                    case "O preço é obrigatório.":
                                    case "O preço deve ser maior que zero.":
                                    case "O preço deve conter apenas números, ponto ou vírgula.":
                                        document.getElementById("mensagemPreco").innerText = error;
                                        break;
                                    default:
                                        document.getElementById("mensagemErro").innerText = error;
                                }
                            });
                        }
                    });
                }
            })
            .then((data) => {
                if (data && data.imagem) {
                    // Acessa a URL da nova imagem atualizada, se disponível
                    const imageUrl = data.imagem;
                    checkImageAvailability("/images/uploads/produtos/" + imageUrl, (available) => {
                        if (available) {
                            fetchProducts();
                        }
                    });
                    $("#modalAtualizacao").modal("hide"); // Esconde o modal de atualização
                }
            })
    });



    $("#modalCadastro").on("show.bs.modal", function (event) {
        var button = $(event.relatedTarget);
        var recipient = button.data("whatever");
        var modal = $(this);
        modal.find(".modal-title").text("Cadastrar produto");
        modal.find(".modal-body input").val(recipient);
    });

    $("#deleteButtonModal").click(function () {

        $("#modalAtualizacao").modal("hide");
        $("#modalDelete").modal("show");

        $("#modalDelete")
            .find(".modal-title")
            .text("Deletar: " + nome);
    });

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
                $("#modalCadastro").modal("hide");
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



        const nome = $("#nomeProdutoCadastro").val();
        const quantidade = $("#quantidadeProdutoCadastro").val();
        const ativo = $("#ativoProduto").prop("checked");
        const preco = parseFloat($("#precoProdutoCadastro").val());
        imagem = $("#imgProdutoCadastro")[0].files[0]; // Captura o arquivo de imagem


        // Função para carregar imagem padrão como Blob
        function carregarImagemPadrao() {
            return fetch('/images/uploads/produtos/padrao.jpeg')
                .then(res => {
                    if (!res.ok) {
                        throw new Error("Erro ao carregar imagem padrão");
                    }
                    return res.blob();
                })
                .then(blob => {
                    // Criar um File a partir do Blob com o nome e tipo desejados
                    return new File([blob], "padrao.jpeg", { type: "image/jpeg" });
                });
        }

        // Verificar se uma imagem foi selecionada ou usar a padrão
        if (!imagem) {
            carregarImagemPadrao().then(padrao => {
                imagem = padrao;
                enviarFormulario(nome, quantidade, ativo, preco, imagem);
            }).catch(error => {
                console.error("Erro ao carregar imagem padrão:", error);
            });
        } else {
            enviarFormulario(nome, quantidade, ativo, preco, imagem);
        }

    });

    function enviarFormulario(nome, quantidade, ativo, preco, imagem) {
        const formData = new FormData();

        // Adicionar imagem ao FormData
        formData.append("file", imagem);
        formData.append("produtoDTO", JSON.stringify({ produto: { nome, quantidade, ativo }, preco: { valor: preco } }));


        fetch("/produtos/cadastro-produto", {
            method: "POST",
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        if (data.errors) {
                            data.errors.forEach(error => {
                                switch (error) {
                                    case "O nome do produto é obrigatório.":
                                        document.getElementById("mensagemNomeCadastro").innerText = error;
                                        break;
                                    case "A quantidade é obrigatória.":
                                    case "A quantidade deve ser maior que zero.":
                                    case "A quantidade deve ser um número inteiro positivo.":
                                        document.getElementById("mensagemQuantidadeCadastro").innerText = error;
                                        break;
                                    case "O preço é obrigatório.":
                                    case "O preço deve ser maior que zero.":
                                    case "O preço deve conter apenas números, ponto ou vírgula.":
                                        document.getElementById("mensagemPrecoCadastro").innerText = error;
                                        break;
                                    default:
                                        document.getElementById("mensagemErro").innerText = error;
                                }
                            });
                        }
                        throw new Error(data.mensagem || "Erro ao atualizar produto.");
                    });
                }
                return response.json();
            })
            .then((data) => {
                if (data && data.imagem) {
                    // Acessa a URL da nova imagem atualizada, se disponível
                    const imageUrl = data.imagem;
                    checkImageAvailability("/images/uploads/produtos/" + imageUrl, (available) => {
                        if (available) {
                            fetchProducts();
                        }
                    });
                    $("#modalCadastro").modal("hide"); // Esconde o modal de atualização
                }
            })
    }

});