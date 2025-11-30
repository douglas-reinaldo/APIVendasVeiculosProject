/**
 * Lógica da página de Vendedores
 * Ocean Breeze Professional - Sistema de Gestão
 */

const API_URL = 'http://localhost:8080/api/vendedores';
let currentVendedor = null;

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    inicializarIcones();
    carregarVendedores();
    configurarEventosModal();
});

/**
 * Carrega a lista de vendedores da API
 */
async function carregarVendedores() {
    const loading = document.getElementById('loading');
    const emptyState = document.getElementById('emptyState');
    const errorState = document.getElementById('errorState');
    const container = document.getElementById('vendedoresContainer');

    exibirElemento(loading, 'flex');
    ocultarElemento(emptyState);
    ocultarElemento(errorState);
    container.innerHTML = '';

    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error(`HTTP Error: ${response.status}`);

        const vendedores = await response.json();
        ocultarElemento(loading);

        if (vendedores.length === 0) {
            exibirElemento(emptyState);
            inicializarIcones();
            return;
        }

        container.innerHTML = vendedores.map((v, index) => criarCardVendedor(v, index)).join('');
        inicializarIcones();
    } catch (error) {
        ocultarElemento(loading);
        exibirElemento(errorState);
        tratarErroRequisicao(error, errorState);
    }
}

/**
 * Cria o HTML de um card de vendedor
 */
function criarCardVendedor(vendedor, index) {
    const numVeiculos = vendedor.numeroVeiculos || 0;
    const plural = numVeiculos !== 1 ? 's' : '';

    return `
        <div class="card animate-slide-up" style="padding: 1.5rem; animation-delay: ${index * 0.1}s;">
            <div style="display: flex; align-items: start; justify-content: space-between; margin-bottom: 1rem;">
                <div style="display: flex; align-items: center; gap: 0.75rem;">
                    <div class="avatar">
                        <i data-lucide="user" style="width: 1.5rem; height: 1.5rem;"></i>
                    </div>
                    <div>
                        <h3 style="font-size: 1.25rem; margin: 0;">${vendedor.nome}</h3>
                        </div>
                </div>
            </div>

            <div style="margin-bottom: 1rem;">
                <div style="display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: hsl(var(--color-muted-foreground));">
                    <i data-lucide="mail" style="width: 1rem; height: 1rem;"></i>
                    <span style="overflow: hidden; text-overflow: ellipsis;">${vendedor.email}</span>
                </div>
            </div>

            <div style="display: flex; align-items: center; justify-content: space-between;">
                <span class="badge">
                    <i data-lucide="car" style="width: 0.875rem; height: 0.875rem;"></i>
                    ${numVeiculos} Veículo${plural}
                </span>

                <div style="display: flex; gap: 0.5rem;">
                    <button class="btn-icon edit" onclick="editarVendedor(${vendedor.id})" title="Editar">
                        <i data-lucide="pencil" style="width: 1rem; height: 1rem;"></i>
                    </button>
                    <button class="btn-icon delete" onclick="deletarVendedor(${vendedor.id})" title="Deletar">
                        <i data-lucide="trash-2" style="width: 1rem; height: 1rem;"></i>
                    </button>
                    <button class="btn btn-primary" style="padding: 0.5rem 0.75rem; font-size: 0.875rem;" onclick="verVeiculos(${vendedor.id}, '${vendedor.nome}')">
                        <i data-lucide="car" style="width: 1rem; height: 1rem;"></i>
                        Ver Carros
                    </button>
                </div>
            </div>
        </div>
    `;
}

/**
 * Abre o modal para criar ou editar vendedor
 */
function openModal(vendedor = null) {
    currentVendedor = vendedor;
    const modal = document.getElementById('modal');
    const form = document.getElementById('formVendedor');
    const modalTitle = document.getElementById('modalTitle');
    const modalDescription = document.getElementById('modalDescription');
    const submitText = document.getElementById('submitText');
    const feedback = document.getElementById('feedback');

    form.reset();
    feedback.innerHTML = '';

    if (vendedor) {
        // Alterado: Exibir o nome em vez do ID no título do modal
        modalTitle.textContent = `Editar Vendedor: ${vendedor.nome}`;
        modalDescription.textContent = 'Atualize as informações do vendedor abaixo.';
        submitText.textContent = 'Atualizar';
        document.getElementById('vendedorId').value = vendedor.id;
        document.getElementById('nome').value = vendedor.nome;
        document.getElementById('email').value = vendedor.email;
        document.getElementById('telefone').value = vendedor.telefone || '';
    } else {
        modalTitle.textContent = 'Cadastrar Novo Vendedor';
        modalDescription.textContent = 'Preencha os dados para cadastrar um novo vendedor.';
        submitText.textContent = 'Cadastrar';
    }

    modal.classList.add('active');
}

/**
 * Fecha o modal
 */
function closeModal() {
    const modal = document.getElementById('modal');
    modal.classList.remove('active');
    currentVendedor = null;
}

/**
 * Busca vendedor por ID para edição
 */
async function editarVendedor(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (response.ok) {
            const vendedor = await response.json();
            openModal(vendedor);
        } else {
            alert('Vendedor não encontrado.');
        }
    } catch (error) {
        alert('Erro ao buscar vendedor.');
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

    const vendedorId = document.getElementById('vendedorId').value;
    const method = vendedorId ? 'PUT' : 'POST';
    const url = vendedorId ? `${API_URL}/${vendedorId}` : API_URL;

    const data = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value,
        telefone: document.getElementById('telefone').value || null
    };

    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok || response.status === 201) {
            feedback.innerHTML = criarAlertaSucesso(`Vendedor ${vendedorId ? 'atualizado' : 'cadastrado'} com sucesso!`);
            inicializarIcones();

            setTimeout(() => {
                closeModal();
                carregarVendedores();
            }, 1500);
        } else if (response.status === 400) {
            feedback.innerHTML = criarAlertaErro('Erro: Verifique os campos ou se o email já está cadastrado.');
            inicializarIcones();
        } else {
            throw new Error('Erro ao salvar');
        }
    } catch (error) {
        feedback.innerHTML = criarAlertaErro('Erro de comunicação com a API.');
        inicializarIcones();
    } finally {
        submitBtn.disabled = false;
        submitText.textContent = vendedorId ? 'Atualizar' : 'Cadastrar';
    }
}

/**
 * Deleta um vendedor
 */
async function deletarVendedor(id) {
    if (!confirm('Tem certeza que deseja deletar este vendedor? Esta ação é irreversível!')) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });

        if (response.status === 204) {
            alert('Vendedor deletado com sucesso!');
            carregarVendedores();
        } else if (response.status === 404) {
            alert('Vendedor não encontrado.');
        } else {
            alert('Erro ao deletar. Verifique se o vendedor não possui dependências.');
        }
    } catch (error) {
        alert('Erro de comunicação ao deletar.');
    }
}

/**
 * Navega para a página de veículos do vendedor
 */
function verVeiculos(id, nome) {
    window.location.href = `veiculos_vendedor.html?vendedorId=${id}&nome=${encodeURIComponent(nome)}`;
}

/**
 * Configura eventos do modal
 */
function configurarEventosModal() {
    const modal = document.getElementById('modal');

    // Fecha ao clicar fora
    modal.addEventListener('click', function(e) {
        if (e.target === this) {
            closeModal();
        }
    });

    // Fecha com ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeModal();
        }
    });
}