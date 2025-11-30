/**
 * Funções utilitárias compartilhadas
 * Ocean Breeze Professional - Sistema de Gestão
 */

/**
 * Formata um valor numérico para moeda brasileira (R$)
 * @param {number} valor - Valor a ser formatado
 * @returns {string} Valor formatado como moeda
 */
function formatarMoeda(valor) {
    const valorNumerico = parseFloat(valor);
    if (isNaN(valorNumerico)) return 'R$ 0,00';

    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(valorNumerico);
}

/**
 * Inicializa os ícones do Lucide após carregar conteúdo dinâmico
 */
function inicializarIcones() {
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }
}

/**
 * Obtém parâmetros da URL
 * @returns {URLSearchParams} Objeto com os parâmetros da URL
 */
function obterParametrosUrl() {
    return new URLSearchParams(window.location.search);
}

/**
 * Exibe um spinner de carregamento
 * @param {HTMLElement} elemento - Elemento onde exibir o spinner
 * @param {string} mensagem - Mensagem opcional
 */
function exibirCarregamento(elemento, mensagem = 'Carregando...') {
    elemento.style.display = 'flex';
    elemento.innerHTML = `
        <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 5rem 1rem;">
            <div class="spinner"></div>
            <p style="margin-top: 1rem; color: hsl(var(--color-muted-foreground));">${mensagem}</p>
        </div>
    `;
}

/**
 * Oculta um elemento
 * @param {HTMLElement} elemento - Elemento a ser ocultado
 */
function ocultarElemento(elemento) {
    elemento.style.display = 'none';
}

/**
 * Exibe um elemento
 * @param {HTMLElement} elemento - Elemento a ser exibido
 * @param {string} display - Tipo de display (block, flex, etc)
 */
function exibirElemento(elemento, display = 'block') {
    elemento.style.display = display;
}

/**
 * Cria um alerta de erro
 * @param {string} mensagem - Mensagem de erro
 * @returns {string} HTML do alerta
 */
function criarAlertaErro(mensagem) {
    return `
        <div class="alert alert-danger">
            <i data-lucide="x-circle" style="width: 1.25rem; height: 1.25rem;"></i>
            <span>${mensagem}</span>
        </div>
    `;
}

/**
 * Cria um alerta de sucesso
 * @param {string} mensagem - Mensagem de sucesso
 * @returns {string} HTML do alerta
 */
function criarAlertaSucesso(mensagem) {
    return `
        <div class="alert alert-success">
            <i data-lucide="check-circle" style="width: 1.25rem; height: 1.25rem;"></i>
            <span>${mensagem}</span>
        </div>
    `;
}

/**
 * Trata erros de requisições HTTP
 * @param {Error} error - Objeto de erro
 * @param {HTMLElement} elemento - Elemento onde exibir o erro
 */
function tratarErroRequisicao(error, elemento) {
    console.error('Erro na requisição:', error);
    elemento.innerHTML = criarAlertaErro('Erro de comunicação com a API. Verifique se está rodando em <strong>localhost:8080</strong>.');
    inicializarIcones();
}
