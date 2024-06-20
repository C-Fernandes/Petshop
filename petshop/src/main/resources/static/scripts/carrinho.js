var total = 0;
var carrinho = []

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
}

function atualizarCarrinho() {
    total = 0;
    totalItens = 0;

    carrinho.forEach(function(produto) {
       total += Number(produto.preco) * produto.qtd;
       totalItens += produto.qtd;
    });

    //atualizar interface
    document.getElementById('total').innerText = total;
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
    console.log(carrinho)
    return undefined;
}

function removerProduto() {

}
