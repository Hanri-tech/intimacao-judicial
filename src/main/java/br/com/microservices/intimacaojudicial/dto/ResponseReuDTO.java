package br.com.microservices.intimacaojudicial.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public record ResponseReuDTO (String nroProcesso, String nomeReu, String mensagem) {
}
