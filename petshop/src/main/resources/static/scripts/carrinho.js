var total = 0;
var carrinho = []
const showCartButton = document.getElementById('carrinho');
const shoppingCart = document.getElementById('shoppingCart');
const finalizarCompra = document.getElementById('finalizarCompra');

function addProduto(nome, preco, id, qtd) {

    if (produtoExisteNoCarrinho(id)) {
        atualizarCarrinho()
    } else {
        qtd = 1;

        var produto = {
            nome: nome,
            preco: preco,
            id: id,
            qtd: qtd
        };

        carrinho.push(produto);

        atualizarCarrinho();
    }

    if (shoppingCart.style.display === 'block') {
        mostrarResumoPedido();
    }
}

function atualizarCarrinho() {
    total = 0;
    totalItens = 0;

    carrinho.forEach(function(produto) {
       total += Number(produto.preco) * produto.qtd;
       totalItens += produto.qtd;
    });

    var elementos = document.querySelectorAll('.total');
    elementos.forEach(function(elemento) {
        elemento.textContent = total;
    });
    document.getElementById('qtd').innerText = totalItens;
}

function produtoExisteNoCarrinho(id) {
    const produtoEncontrado = carrinho.find(produto => produto.id === id);
    if (produtoEncontrado) {
        produtoEncontrado.qtd++;
        return produtoEncontrado;
    }
    return undefined;
}

function removerProduto(id) {
    if(carrinho.length == 0) total = 0;
    carrinho = carrinho.filter(produto => Number(produto.id) !== Number(id));
    atualizarCarrinho();
    mostrarResumoPedido();
}

function removerItem(id) {
    var index = carrinho.findIndex(produto => Number(produto.id) === Number(id));
    if (index !== -1 && carrinho[index].qtd > 1) {
        carrinho[index].qtd--;
    }
    atualizarCarrinho();
    mostrarResumoPedido();
}

showCartButton.addEventListener('click', () => {
    mostrarResumoPedido();
    shoppingCart.style.display = 'block';
});

function mostrarResumoPedido() {
    var tbody = document.getElementById('cartBody');
    tbody.innerHTML = '';

    for (var i = 0; i < carrinho.length; i++) {
        var item = carrinho[i];

        var newRow = document.createElement('tr');
        newRow.innerHTML = `
          <th scope="row">${i + 1}</th>
          <td>${item.nome}</td>
          <td>${item.preco}</td>
          <td>
            <button class="btn btn-sm btn-outline-primary" onclick="removerItem(${item.id})">-</button>
            <span>${item.qtd}</span>
            <button class="btn btn-sm btn-outline-primary" onclick="addProduto('${item.nome}', '${item.preco}', '${item.id}', ${item.qtd + 1})">+</button>
          </td>
          <td>
            <button class="btn btn-danger" onclick="removerProduto(${item.id})">Remover</button>
           </td>
        `;
        tbody.appendChild(newRow);
    }
}


finalizarCompra.addEventListener('click', () => {
    const funcionarioId = document.getElementById("funcionario").value;
    const clienteId = document.getElementById("cliente").value;
    console.log(funcionarioId, clienteId)
    const carrinhoDTO = carrinho.map(item => ({
        nome: item.nome,
        quantidade: item.qtd,
        id: item.id,
        preco: item.preco
    }));


    fetch('/pedido/create/${funcionarioId}/${clienteId}', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            valor: total,
            data: new Date(),
            status: "ativo",
            carrinhoDTO: carrinhoDTO,
            funcionarioId: funcionarioId,
            clienteId: clienteId
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errors => {
                    errors.forEach(error => {
                        console(error);
                    });
                });
            } else{
                window.location.href = '/pedido/list';
            }
        })
})