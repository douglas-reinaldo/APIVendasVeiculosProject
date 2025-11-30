/**
 * Lógica da página de Histórico de Vendas
 * Ocean Breeze Professional - Sistema de Gestão
 */

const API_URL = 'http://localhost:8080/api/vendas';

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    inicializarIcones();
    carregarVendas();
});

/**
 * Carrega o histórico completo de vendas
 */
async function carregarVendas() {
    const loading = document.getElementById('loading');
    const emptyState = document.getElementById('emptyState');
    const errorState = document.getElementById('errorState');
    const tableContainer = document.getElementById('tableContainer');
    const tbody = document.getElementById('vendasTbody');

    exibirElemento(loading, 'flex');
    ocultarElemento(emptyState);
    ocultarElemento(errorState);
    ocultarElemento(tableContainer);
    tbody.innerHTML = '';

    try {
        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        const vendas = await response.json();
        ocultarElemento(loading);

        if (vendas.length === 0) {
            exibirElemento(emptyState);
            inicializarIcones();
            return;
        }

        exibirElemento(tableContainer);
        tbody.innerHTML = vendas.map((venda, index) => criarLinhaVenda(venda, index)).join('');
        inicializarIcones();
    } catch (error) {
        console.error('Erro ao carregar vendas:', error);
        ocultarElemento(loading);
        exibirElemento(errorState);
        tratarErroRequisicao(error, errorState);
    }
}

/**
 * Cria o HTML de uma linha da tabela de vendas
 */
function criarLinhaVenda(venda, index) {
    const vendedorNome = venda.vendedorNome || 'N/A';
    const veiculoModelo = venda.veiculoModelo || 'N/A';

    const valorNumerico = parseFloat(venda.valorFinal);
    let valorFormatado;

    if (isNaN(valorNumerico) || valorNumerico === null) {
        valorFormatado = 'R$ 0,00';
    } else {
        valorFormatado = formatarMoeda(valorNumerico);
    }

    const dataFormatada = new Date(venda.dataVenda).toLocaleDateString('pt-BR', { timeZone: 'UTC' });

    return `
        <tr style="animation-delay: ${index * 0.05}s;">
            <td>
                <span class="id-badge">${venda.id}</span>
            </td>
            <td>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <i data-lucide="user" style="width: 1rem; height: 1rem; color: hsl(var(--color-muted-foreground));"></i>
                    <span style="font-weight: 500;">${vendedorNome}</span>
                </div>
            </td>
            <td>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <i data-lucide="car" style="width: 1rem; height: 1rem; color: hsl(var(--color-muted-foreground));"></i>
                    <span>${veiculoModelo}</span>
                </div>
            </td>
            <td class="valor-cell">${valorFormatado}</td>
            <td>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <i data-lucide="calendar" style="width: 1rem; height: 1rem; color: hsl(var(--color-muted-foreground));"></i>
                    <span>${dataFormatada}</span>
                </div>
            </td>
        </tr>
    `;
}
