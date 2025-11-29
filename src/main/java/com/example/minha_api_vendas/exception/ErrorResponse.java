package com.example.minha_api_vendas.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String mensagem;
    private Map<String, String> erros;

    public ErrorResponse(int status, String mensagem, Map<String, String> erros) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.mensagem = mensagem;
        this.erros = erros;
    }

    public ErrorResponse(int status, String mensagem) {
        this(status, mensagem, null);
    }
}