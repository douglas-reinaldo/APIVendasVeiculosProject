/**
 * Lógica da página de Veículos do Vendedor
 * Ocean Breeze Professional - Sistema de Gestão
 */

const API_URL_BASE = 'http://localhost:8080/api/vendedores';
const API_URL_VEICULOS = 'http://localhost:8080/api/veiculos';

let vendedorId = null;
let vendedorNome = '';
let currentVeiculo = null;

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    inicializarIcones();
    carregarVeiculosDoVendedor();
    configurarEventosModal();
});

/**
 * Carrega os veículos de um vendedor específico
 */
async function carregarVeiculosDoVendedor() {
    const params = obterParametrosUrl();
    vendedorId = params.get('vendedorId');
    vendedorNome = params.get('nome');

    if (!vendedorId) {
        document.getElementById('loading').innerHTML = criarAlertaErro('ID do vendedor não fornecido.');
        inicializarIcones();
        return;
    }

    document.getElementById('vendedorNome').textContent = vendedorNome || `ID ${vendedorId}`;

    const loading = document.getElementById('loading');
    const emptyState = document.getElementById('emptyState');
    const errorState = document.getElementById('errorState');
    const container = document.getElementById('veiculosContainer');

    exibirElemento(loading, 'flex');
    ocultarElemento(emptyState);
    ocultarElemento(errorState);
    container.innerHTML = '';

    try {
        const response = await fetch(`${API_URL_BASE}/${vendedorId}/veiculos`);

        if (response.status === 404) {
            loading.innerHTML = '<div class="alert alert-warning"><i data-lucide="alert-triangle"></i> Vendedor não encontrado.</div>';
            inicializarIcones();
            return;
        }

        const veiculos = await response.json();
        ocultarElemento(loading);

        if (veiculos.length === 0) {
            exibirElemento(emptyState);
            inicializarIcones();
            return;
        }

        container.innerHTML = veiculos.map((v, index) => criarCardVeiculo(v, index)).join('');
        inicializarIcones();
    } catch (error) {
        ocultarElemento(loading);
        exibirElemento(errorState);
        tratarErroRequisicao(error, errorState);
    }
}

/**
 * Cria o HTML de um card de veículo
 */
function criarCardVeiculo(veiculo, index) {
    const isVendido = veiculo.vendido;
    const statusBadge = isVendido
        ? '<span class="badge badge-danger"><i data-lucide="check-circle" style="width: 0.875rem; height: 0.875rem;"></i> VENDIDO</span>'
        : '<span class="badge badge-success"><i data-lucide="tag" style="width: 0.875rem; height: 0.875rem;"></i> À Venda</span>';

    // Botões de Ação para veículo à venda
    const activeButtons = `
        <button class="btn-icon edit" onclick="editarVeiculo(${veiculo.id})" title="Editar">
            <i data-lucide="pencil" style="width: 1rem; height: 1rem;"></i>
        </button>
        <button class="btn-icon delete" onclick="deletarVeiculo(${veiculo.id})" title="Deletar">
            <i data-lucide="trash-2" style="width: 1rem; height: 1rem;"></i>
        </button>
    `;

    // Botões de Ação DESABILITADOS para veículo vendido
    const disabledButtons = `
        <button class="btn-icon disabled" title="Veículo Vendido - Não pode ser editado ou deletado" disabled>
            <i data-lucide="pencil" style="width: 1rem; height: 1rem;"></i>
        </button>
        <button class="btn-icon disabled" title="Veículo Vendido - Não pode ser editado ou deletado" disabled>
            <i data-lucide="trash-2" style="width: 1rem; height: 1rem;"></i>
        </button>
    `;

    return `
        <div class="card ${isVendido ? 'vendido' : ''} animate-slide-up" style="padding: 1.5rem; animation-delay: ${index * 0.1}s;">
            <div style="display: flex; align-items: start; justify-content: space-between; margin-bottom: 1rem;">
                <div>
                    <h3 style="font-size: 1.25rem; margin: 0;">${veiculo.modelo} (${veiculo.ano})</h3>
                    <p style="font-size: 0.875rem; color: hsl(var(--color-muted-foreground)); margin: 0.25rem 0 0 0;">
                        ${veiculo.marca}
                    </p>
                </div>
                ${statusBadge}
            </div>

            <div style="margin-bottom: 1rem;">
                <div style="display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: hsl(var(--color-success)); margin-bottom: 0.5rem;">
                    <i data-lucide="dollar-sign" style="width: 1rem; height: 1rem;"></i>
                    <span style="font-weight: 600; color: hsl(var(--color-success)); font-size: 1.125rem;">${formatarMoeda(veiculo.preco)}</span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: hsl(var(--color-muted-foreground));">
                    <i data-lucide="hash" style="width: 1rem; height: 1rem;"></i>
                    <span>${veiculo.placa}</span>
                </div>
            </div>

            <div style="display: flex; align-items: center; justify-content: space-between; gap: 0.5rem;">
                ${!isVendido ? `
                    <button class="btn btn-success" style="padding: 0.5rem 0.75rem; font-size: 0.875rem;"
                            onclick="abrirConfirmacaoVenda(${vendedorId}, '${vendedorNome}', ${veiculo.id}, '${veiculo.modelo}', ${veiculo.preco})">
                        <i data-lucide="shopping-cart" style="width: 1rem; height: 1rem;"></i>
                        Vender
                    </button>
                ` : '<div></div>'}

                <div style="display: flex; gap: 0.5rem;">
                    ${isVendido ? disabledButtons : activeButtons}
                </div>
            </div>
        </div>
    `;
}

/**
 * Abre o modal para criar ou editar veículo
 */
function openModal(veiculo = null) {
    currentVeiculo = veiculo;
    const modal = document.getElementById('modal');
    const form = document.getElementById('formVeiculo');
    const modalTitle = document.getElementById('modalTitle');
    const modalDescription = document.getElementById('modalDescription');
    const submitText = document.getElementById('submitText');
    const feedback = document.getElementById('feedback');
    const vendedorIdContainer = document.getElementById('vendedorIdContainer');
    const vendedorIdInput = document.getElementById('vendedorIdInput');

    form.reset();
    feedback.innerHTML = '';

    if (veiculo) {
        modalTitle.textContent = `Editar Veículo #${veiculo.id}`;
        modalDescription.textContent = 'Atualize as informações do veículo.';
        submitText.textContent = 'Atualizar';
        document.getElementById('veiculoId').value = veiculo.id;
        document.getElementById('marca').value = veiculo.marca;
        document.getElementById('modelo').value = veiculo.modelo;
        document.getElementById('ano').value = veiculo.ano;
        document.getElementById('preco').value = veiculo.preco;
        document.getElementById('placa').value = veiculo.placa;
        document.getElementById('placa').disabled = true;

        vendedorIdContainer.style.display = 'block';
        vendedorIdInput.value = veiculo.vendedorId;
        vendedorIdInput.required = true;
    } else {
        modalTitle.textContent = 'Cadastrar Novo Veículo';
        modalDescription.textContent = 'Preencha os dados do veículo.';
        submitText.textContent = 'Cadastrar';
        document.getElementById('placa').disabled = false;

        vendedorIdContainer.style.display = 'none';
        vendedorIdInput.value = '';
        vendedorIdInput.required = false;
    }

    modal.classList.add('active');
}

/**
 * Fecha o modal
 */
function closeModal() {
    const modal = document.getElementById('modal');
    modal.classList.remove('active');
    currentVeiculo = null;
}

/**
 * Busca veículo por ID para edição
 */
async function editarVeiculo(id) {
    try {
        const response = await fetch(`${API_URL_VEICULOS}/${id}`);
        if (response.ok) {
            const veiculo = await response.json();

            // ⚠️ Nova checagem: Se o veículo estiver vendido, não abre o modal de edição.
            if (veiculo.vendido) {
                alert('Este veículo já foi vendido e não pode ser editado.');
                return;
            }

            openModal(veiculo);
        } else {
            alert('Veículo não encontrado.');
        }
    } catch (error) {
        alert('Erro ao buscar veículo.');
    }
}

/**
 * Processa o envio do formulário
 */
async function handleSubmit(event) {
    event.preventDefault();

    const submitBtn = document.getElementById('submitBtn');
    const submitText = document.getElementById('submitText');
    const feedback = document.getElementById('feedback');

    submitBtn.disabled = true;
    submitText.innerHTML = '<div class="spinner" style="width: 1rem; height: 1rem; border-width: 2px;"></div> Salvando...';

    const veiculoId = document.getElementById('veiculoId').value;
    const method = veiculoId ? 'PUT' : 'POST';
    const url = veiculoId ? `${API_URL_VEICULOS}/${veiculoId}` : API_URL_VEICULOS;

    let finalVendedorId;
    if (veiculoId) {
        finalVendedorId = parseInt(document.getElementById('vendedorIdInput').value);
    } else {
        finalVendedorId = parseInt(vendedorId);
    }

    const data = {
        marca: document.getElementById('marca').value,
        modelo: document.getElementById('modelo').value,
        ano: parseInt(document.getElementById('ano').value),
        preco: parseFloat(document.getElementById('preco').value),
        placa: document.getElementById('placa').value,
        vendedorId: finalVendedorId
    };

    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok || response.status === 201) {
            feedback.innerHTML = criarAlertaSucesso(`Veículo ${veiculoId ? 'atualizado' : 'cadastrado'} com sucesso!`);
            inicializarIcones();

            setTimeout(() => {
                closeModal();
                carregarVeiculosDoVendedor();
            }, 1500);
        } else if (response.status === 400) {
            feedback.innerHTML = criarAlertaErro('Erro: Verifique se a placa já existe ou se todos os campos estão preenchidos.');
            inicializarIcones();
        } else {
            throw new Error('Erro ao salvar');
        }
    } catch (error) {
        feedback.innerHTML = criarAlertaErro('Erro de comunicação com a API.');
        inicializarIcones();
    } finally {
        submitBtn.disabled = false;
        submitText.textContent = veiculoId ? 'Atualizar' : 'Cadastrar';
    }
}

/**
 * Deleta um veículo
 */
async function deletarVeiculo(id) {
    if (!confirm('Tem certeza que deseja deletar este veículo? Isso será possível apenas se não houver vendas registradas.')) return;

    try {
        const response = await fetch(`${API_URL_VEICULOS}/${id}`, { method: 'DELETE' });

        if (response.status === 204) {
            alert('Veículo deletado com sucesso!');
            carregarVeiculosDoVendedor();
        } else if (response.status === 404) {
            alert('Veículo não encontrado.');
        } else if (response.status === 409) {
            alert('Não foi possível deletar: O veículo possui vendas registradas.');
        } else {
            alert('Erro ao deletar veículo.');
        }
    } catch (error) {
        alert('Erro de comunicação ao deletar.');
    }
}

/**
 * Navega para a página de confirmação de venda
 */
function abrirConfirmacaoVenda(vendedorId, vendedorNome, veiculoId, veiculoModelo, preco) {
    const precoNumerico = parseFloat(preco);

    if (isNaN(precoNumerico)) {
        const precoLimpo = String(preco).replace('R$', '').replace(/\./g, '').replace(',', '.').trim();
        const precoFinal = parseFloat(precoLimpo);

        if (isNaN(precoFinal)) {
            alert('Erro: Preço inválido para iniciar a venda.');
            return;
        }

        const url = `confirmar_venda.html?vendedorId=${vendedorId}&vendedorNome=${encodeURIComponent(vendedorNome)}&veiculoId=${veiculoId}&veiculoModelo=${encodeURIComponent(veiculoModelo)}&preco=${precoFinal}`;
        window.location.href = url;
        return;
    }

    const url = `confirmar_venda.html?vendedorId=${vendedorId}&vendedorNome=${encodeURIComponent(vendedorNome)}&veiculoId=${veiculoId}&veiculoModelo=${encodeURIComponent(veiculoModelo)}&preco=${precoNumerico}`;
    window.location.href = url;
}

/**
 * Configura eventos do modal
 */
function configurarEventosModal() {
    const modal = document.getElementById('modal');

    modal.addEventListener('click', function(e) {
        if (e.target === this) {
            closeModal();
        }
    });

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeModal();
        }
    });
}