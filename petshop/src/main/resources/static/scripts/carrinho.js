var total = 0;
var carrinho = []
const showCartButton = document.getElementById('carrinho');
const shoppingCart = document.getElementById('shoppingCart');

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
    console.log('Carrinho atualizado:', carrinho);
    console.log('Total:', total);
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
    carrinho = carrinho.filter(produto => Number(produto.id) !== Number(id));
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
            <button class="btn btn-sm btn-outline-primary" onclick="removerItem(this)">-</button>
            <span>${item.qtd}</span>
            <button class="btn btn-sm btn-outline-primary" onclick="adicionarItem(this)">+</button>
          </td>
          <td>
            <button class="btn btn-sm btn-outline-primary" onclick="removerProduto(${item.id})">-</button>
           </td>
        `;
        tbody.appendChild(newRow);
    }
}