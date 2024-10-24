package br.com.microservices.intimacaojudicial.dto;

import br.com.microservices.intimacaojudicial.entity.Reu;

import java.util.List;

public record ResponseProcessoDTO(String nroProcesso, List<?> reu) {
}
