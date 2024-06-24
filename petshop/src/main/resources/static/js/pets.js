
var idPet = 0, dataNascimento, especie = "", raca = "", imagem = "", sexo = "", nome = "";


function fetchPets() {
    fetch("/pets/listar", {
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            var petContainer = document.getElementById("product-container");
            petContainer.innerHTML = "";

            if (data.length === 0) {
                var mensagem = document.createElement("p");
                mensagem.textContent = "Nenhum pet encontrado.";
                petContainer.appendChild(mensagem);
            } else {
                data.forEach(pet => {
                    var card = document.createElement("div");
                    card.className = "card div-editar";

                    var imgSrc = "/images/uploads/pets/" + pet.imagem; // Caminho da imagem do pet

                    var idadeText = "";
                    if (pet.idade != null) {
                        if (pet.idade >= 12) {
                            var anos = Math.floor(pet.idade / 12);
                            var meses = pet.idade % 12;
                            idadeText = `${anos} ano${anos > 1 ? 's' : ''}`;
                            if (meses > 0) {
                                idadeText += ` e ${meses} mês${meses > 1 ? 'es' : ''}`;
                            }
                        } else if (pet.idade > 0) {
                            idadeText = `${pet.idade} mês${pet.idade > 1 ? 'es' : ''}`;
                        }
                    }
                    card.innerHTML = `
                    
                    <img src="${imgSrc}" alt="Imagem do Pet" />
                
                    <div class="card-body">
                        <h3 class="card-title">${pet.nome}</h3>
                        <div class="card-text">
                            <p>Data de nascimento: <span>${pet.dataDeNascimento}</span></p>
                            <div id="raca">
                                <p><span>${pet.raca.raca}</span></p>
                                <p>-</p>
                                <p><span>${pet.raca.especie}</span></p>
                            </div>
                             <div id="raca">
                            <p><span>${pet.sexo === 'M' ? 'Macho' : 'Fêmea'}</span></p>
                                 <p>-</p> <p> <span>${pet.peso}</span> kg</p></div>
                              
                                <p>${pet.idade != null ? '<span>' + idadeText + '</span>' : ''}</p>
                           
                           </div>
                    </div>
                    <div class="icons">
                        <i class="fas fa-edit editar"
                            src="/images/editar.jpg"
                            alt="Editar pet"
                            data-id="${pet.id}"
                            data-nome="${pet.nome}"
                            data-dataDeNascimento="${pet.dataDeNascimento}"
                            data-idade="${pet.idade}"
                            data-raca-raca="${pet.raca.raca}"
                            data-raca-especie="${pet.raca.especie}"
                            data-imagem="${pet.imagem}"
                            data-peso="${pet.peso}"
                            data-sexo="${pet.sexo}"></i>
                        <i class="fas fa-trash-alt deleteButtonModal"
                            data-id="${pet.id}"
                            src="/images/lixeiraDelete.webp"></i>
                    </div>
                `;
                    petContainer.appendChild(card);
                });
            }
        })
        .catch(error => console.error("Erro ao buscar pets:", error));
}



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

    fetchPets();


    document.getElementById("inicio").addEventListener("click", function () {
        window.location.href = "/usuario/inicial";
    });
    document.getElementById("produtos").addEventListener("click", function () {
        window.location.href = "";
    }); document.getElementById("servicos").addEventListener("click", function () {
        window.location.href = "";
    }); document.getElementById("meu-usuario").addEventListener("click", function () {
        window.location.href = "/usuario/meus-dados-cliente";
    });


    $("#atualizarPetButton").click(function () {
        nome = $("#nomePet").val();
        const peso = parseFloat($("#pesoPet").val());
        const formData = new FormData();

        // Verifica se o elemento existe e se foi selecionado um arquivo
        if ($("#imgPet")[0] && $("#imgPet")[0].files[0]) {
            formData.append("file", $("#imgPet")[0].files[0]);
        }
        const petDTO = {
            pet: { id: idPet, nome: nome, dataDeNascimento: dataNascimento, peso: peso, sexo: sexo },
            raca: {
                raca,
                especie
            }
        };

        formData.append("petDTO", JSON.stringify(petDTO));

        fetch("/pets/atualizar", {
            method: "PUT",
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        if (data.errors) {
                            data.errors.forEach(error => {
                                switch (error) {
                                    case "O nome do pet é obrigatório.":
                                        document.getElementById("mensagemNomePet").innerText = error;
                                        break;
                                    case "A raça do pet é obrigatória.":
                                        document.getElementById("mensagemRacaPet").innerText = error;
                                        break;
                                    case "A espécie do pet é obrigatória.":
                                        document.getElementById("mensagemEspeciePet").innerText = error;
                                        break;
                                    case "O peso do pet é obrigatório.":
                                    case "O peso do pet deve ser maior que zero.":
                                        document.getElementById("mensagemPesoPet").innerText = error;
                                        break;
                                    case "O sexo do pet é obrigatório e deve ser 'M' (macho) ou 'F' (fêmea).":
                                        document.getElementById("mensagemSexoPet").innerText = error;
                                        break;
                                    default:
                                        console.error("Erro desconhecido:", error);
                                }
                            });
                        }
                        // Retorna null para que o próximo then não seja executado com data undefined
                        return null;
                    });
                } else {
                    // Retorna os dados da resposta se a requisição foi bem sucedida
                    return response.json();
                }
            })
            .then((data) => {
                if (data && data.imagem) {
                    // Acessa a URL da nova imagem atualizada, se disponível
                    const imageUrl = data.imagem;
                    console.log("chegou aqui ");
                    checkImageAvailability("/images/uploads/pets/" + imageUrl, (available) => {
                        if (available) {
                            fetchPets();
                        }
                    });

                    $("#modalPet").modal("hide"); // Esconde o modal de atualização
                }
            })

    });

    $("#buscar").click(function () {
        const filtroNome = $("#filtroNome").val().trim();
        if (filtroNome !== "") {
            // Filtrar pets pelo nome
            fetch(`/pets/buscar?nome=${filtroNome}`)
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Erro ao buscar pets.");
                    }
                    return response.json();
                })
                .then((data) => {
                    var petContainer = document.getElementById("product-container");
                    petContainer.innerHTML = "";
                    if (data.length === 0) {
                        var mensagem = document.createElement("p");
                        mensagem.innerText = "Nenhum pet encontrado.";
                        petContainer.appendChild(mensagem);
                    } else {
                        data.forEach((pet) => {
                            var card = document.createElement("div");
                            card.className = "card div-editar";
                            card.setAttribute("data-id", pet.id);
                            card.setAttribute("data-nome", pet.nome);
                            card.setAttribute("data-dataDeNascimento", pet.dataDeNascimento);
                            card.setAttribute("data-idade", pet.idade);
                            card.setAttribute("data-raca-raca", pet.raca.raca);
                            card.setAttribute("data-raca-especie", pet.raca.especie);
                            card.setAttribute("data-imagem", pet.imagem);
                            card.setAttribute("data-peso", pet.peso);
                            card.setAttribute("data-sexo", pet.sexo);
                            var img = "/images/uploads/pets/" + pet.imagem;
                            console.log("Imagem: " + img);
                            var idadeText = "";
                            if (pet.idade != null) {
                                if (pet.idade >= 12) {
                                    idadeText = `${Math.floor(pet.idade / 12)} ano${pet.idade >= 24 ? 's' : ''} e ${pet.idade % 12} mês${pet.idade % 12 > 1 ? 'es' : ''}`;
                                } else {
                                    idadeText = `${pet.idade} mês${pet.idade > 1 ? 'es' : ''}`;
                                }
                            }
                            card.innerHTML = `
                            <img src="${img}" alt="Imagem do Pet" accept="image/*" />
                            <div class="card-body">
                                <h5 class="card-title">${pet.nome}</h5>
                                <div class="card-text">
                                    <p>Data de nascimento: <span>${pet.dataDeNascimento}</span></p>
                                    <div id="raca">
                                        <p>Raça: <span>${pet.raca.raca}</span></p>
                                        <p>-</p>
                                        <p>Espécie: <span>${pet.raca.especie}</span></p>
                                    </div>
                                    <p>${pet.idade != null ? '<span>' + idadeText + '</span>' : ''}</p>
                                    <div id="raca">
                                        <p><span>${pet.sexo === 'M' ? 'Macho' : 'Fêmea'}</span></p>
                                        <p>-</p>
                                        <p> <span>${pet.peso}</span> kg</p>
                                    </div>
                                </div>
                            </div>
                            <div class="icons">
                                <i class="fas fa-edit editar"
                                    src="/images/editar.jpg"
                                    alt="Editar pet"
                                    data-id="${pet.id}"
                                    data-nome="${pet.nome}"
                                    data-dataDeNascimento="${pet.dataDeNascimento}"
                                    data-idade="${pet.idade}"
                                    data-raca-raca="${pet.raca.raca}"
                                    data-raca-especie="${pet.raca.especie}"
                                    data-imagem="${pet.imagem}"
                                    data-peso="${pet.peso}"
                                    data-sexo="${pet.sexo}"></i>
                                <i class="fas fa-trash-alt deleteButtonModal"
                                    data-id="${pet.id}"
                                    src="/images/lixeiraDelete.webp"></i>
                            </div>`;
                            petContainer.appendChild(card);
                        });
                    }
                })
                .catch((error) => console.error("Erro ao buscar pets:", error));
        } else {
            // Se o campo de filtro estiver vazio, buscar todos os pets
            fetchPets();
        }
    });
    $("#abrirModalPet").click(function () {
        $("#atualizarPetButton").hide();

        $("#petButton").show();
        $("#modalPet").modal("show");

    })
    $(document).on('click', '.editar', function () {
        idPet = $(this).data("id");
        nome = $(this).data("nome");
        especie = $(this).data("especie");
        raca = $(this).data("raca");
        var peso = $(this).data("peso");
        dataNascimento = $(this).data("dataDeNascimento");

        // Preenche os campos do modal com os dados do pet
        $('#nomePet').val(nome);
        $('#pesoPet').val(peso);
        // Define a imagem, se necessário

        // Modifica o título do modal para "Atualizar Pet"
        $('#modalPet .modal-title').text('Atualizar Pet');

        // Esconde todos os outros campos exceto o campo de nome
        $('#modalPet #especiePet').closest('.form-group').hide();
        $('#modalPet #racaPet').closest('.form-group').hide();
        $('#modalPet #dataNascimentoPet').closest('.form-group').hide();
        $('#modalPet #sexoPet').closest('.form-group').hide();

        $("#atualizarPetButton").show();

        $("#petButton").hide();
        // Abre o modal de edição de pet
        $('#modalPet').modal('show');


    });

    $("#petButton").click(function () {

        // Modifica o título do modal para "Atualizar Pet"
        $('#modalPet .modal-title').text('Cadastro Pet');

        // Esconde todos os outros campos exceto o campo de nome
        $('#modalPet #especiePet').closest('.form-group').show();
        $('#modalPet #racaPet').closest('.form-group').show();
        $('#modalPet #dataNascimentoPet').closest('.form-group').show();
        $('#modalPet #sexoPet').closest('.form-group').show();

        $("#atualizarPetButton").hide();
        $("#petButton").show();
        nome = $("#nomePet").val();
        especie = $("#especiePet").val();
        raca = $("#racaPet").val();
        dataNascimento = $("#dataNascimentoPet").val();
        sexo = $("#sexoPet").val();
        const peso = parseFloat($("#pesoPet").val());
        const formData = new FormData();

        // Verifica se o elemento existe e se foi selecionado um arquivo
        if ($("#imgPet")[0] && $("#imgPet")[0].files[0]) {
            formData.append("file", $("#imgPet")[0].files[0]);
        }

        // Montar objeto para envio
        const petDTO = {
            pet: {

                nome: nome,
                dataDeNascimento: dataNascimento,
                sexo: sexo,
                peso: peso
            },
            raca: {
                raca: raca,
                especie: especie
            }
        };

        // Adicionar o objeto ao FormData
        formData.append("petDTO", JSON.stringify(petDTO));

        // Requisição POST para cadastrar o pet
        fetch("/pets/cadastro-pet", {
            method: "POST",
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.mensagem || "Erro ao cadastrar o pet.");
                    });
                }
                return response.json(); // Converte a resposta para JSON
            })
            .then(data => {
                // Acessa a URL da nova imagem atualizada, se disponível
                const imageUrl = data.imagem;
                checkImageAvailability("/images/uploads/pets/" + imageUrl, (available) => {
                    if (available) {
                        fetchPets(); // Função para atualizar a lista de pets na interface
                    }
                });
                $("#modalPet").modal("hide"); // Esconde o modal de cadastro
            })
            .catch(error => {
                console.error("Erro ao cadastrar pet:", error); // Exibe o erro no console
                // Aqui você pode exibir uma mensagem de erro na interface, se desejar
            });
    });
    $(document).on('click', '.deleteButtonModal', function () {

        $("#modalAtualizacao").modal("hide");
        $("#modalDelete").modal("show");

        $("#modalDelete")
            .find(".modal-title")
            .text("Deletar: " + nome);
    });
    $("#deleteButton").click(function () {

        fetch(`/pets/delete/${idPet}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Erro ao deletar pet.");
                }
                fetchPets(); // Função que atualiza a lista de pets após a exclusão

                return response;
            })
            .then((data) => {
                console.log("Pet deletado com sucesso:", data);
                // Ocultar o modal ou executar outra ação após a exclusão
                $("#modalPet").modal("hide");
            })
            .catch((error) => {
                console.error("Erro ao deletar pet:", error);
            });
    });

});
