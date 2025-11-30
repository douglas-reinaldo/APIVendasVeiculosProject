/**
 * Lógica da página de Confirmação de Venda
 * Ocean Breeze Professional - Sistema de Gestão
 */

const API_URL_VENDAS = 'http://localhost:8080/api/vendas';
let VENDEDOR_ID = null;
let VEICULO_ID = null;
let PRECO_VEICULO = 0.0;

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    inicializarIcones();
    preencherDados();
});

/**
 * Preenche os dados da venda a partir da URL
 */
function preencherDados() {
    const params = obterParametrosUrl();

    VENDEDOR_ID = params.get('vendedorId');
    VEICULO_ID = params.get('veiculoId');
    const vendedorNome = params.get('vendedorNome');
    const veiculoModelo = params.get('veiculoModelo');

    const precoParam = params.get('preco');
    const precoUrl = parseFloat(precoParam);

    let isValid = true;
    let erroMsg = 'Dados incompletos para a venda. ';

    if (!VENDEDOR_ID || !VEICULO_ID) {
        isValid = false;
        erroMsg += '(IDs ausentes). ';
    }
    if (isNaN(precoUrl) || precoParam === null || precoParam === 'undefined') {
        isValid = false;
        erroMsg += `(Preço inválido ou ausente). `;
    }

    document.getElementById('infoVendedor').textContent = decodeURIComponent(vendedorNome || 'Vendedor Desconhecido');
    document.getElementById('infoVeiculo').textContent = decodeURIComponent(veiculoModelo || 'Veículo Desconhecido');
    document.getElementById('infoPreco').textContent = formatarMoeda(precoUrl);

    document.getElementById('vendedorIdInfo').textContent = VENDEDOR_ID || '???';
    document.getElementById('veiculoIdInfo').textContent = VEICULO_ID || '???';

    if (!isValid) {
        document.getElementById('feedbackVenda').innerHTML = `
            <div class="alert alert-danger">
                <i data-lucide="x-circle" style="width: 1.25rem; height: 1.25rem;"></i>
                <span>${erroMsg} <strong>Volte e selecione novamente.</strong></span>
            </div>
        `;
        document.getElementById('btnConfirmar').disabled = true;
        document.getElementById('infoPreco').textContent = 'Valor Indisponível';
        inicializarIcones();
        return;
    }

    PRECO_VEICULO = precoUrl;
}

/**
 * Registra a venda na API
 */
async function registrarVenda() {
    const btn = document.getElementById('btnConfirmar');
    const feedbackDiv = document.getElementById('feedbackVenda');

    btn.disabled = true;
    btn.innerHTML = '<div class="spinner"></div> Registrando venda...';
    feedbackDiv.innerHTML = '';

    const vendaInputDTO = {
        veiculoId: parseInt(VEICULO_ID),
        vendedorId: parseInt(VENDEDOR_ID),
        valorTotal: PRECO_VEICULO
    };

    try {
        const response = await fetch(API_URL_VENDAS, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(vendaInputDTO)
        });

        if (response.status === 201) {
            const novaVenda = await response.json();
            const valorVendaFormatado = formatarMoeda(novaVenda.valorFinal || PRECO_VEICULO);

            feedbackDiv.innerHTML = `
                <div class="alert alert-success">
                    <i data-lucide="check-circle" style="width: 1.25rem; height: 1.25rem;"></i>
                    <span><strong>Venda #${novaVenda.id} registrada com sucesso!</strong><br>Valor: ${valorVendaFormatado}</span>
                </div>
            `;
            inicializarIcones();

            btn.classList.remove('btn-success');
            btn.classList.add('btn-outline');
            btn.innerHTML = '<i data-lucide="arrow-left" style="width: 1rem; height: 1rem;"></i> Voltar à Lista de Vendedores';
            btn.onclick = () => window.location.href = 'vendedores.html';
            btn.disabled = false;
            inicializarIcones();

        } else {
            feedbackDiv.innerHTML = `
                <div class="alert alert-danger">
                    <i data-lucide="x-octagon" style="width: 1.25rem; height: 1.25rem;"></i>
                    <span>Erro ao registrar venda (Status: ${response.status}). O veículo já pode ter sido vendido.</span>
                </div>
            `;
            inicializarIcones();
            btn.disabled = false;
            btn.innerHTML = '<i data-lucide="check-circle" style="width: 1.25rem; height: 1.25rem;"></i> CONFIRMAR VENDA AGORA';
            inicializarIcones();
        }
    } catch (error) {
        feedbackDiv.innerHTML = criarAlertaErro('Erro de comunicação com a API.');
        inicializarIcones();
        btn.disabled = false;
        btn.innerHTML = '<i data-lucide="check-circle" style="width: 1.25rem; height: 1.25rem;"></i> CONFIRMAR VENDA AGORA';
        inicializarIcones();
    }
}
