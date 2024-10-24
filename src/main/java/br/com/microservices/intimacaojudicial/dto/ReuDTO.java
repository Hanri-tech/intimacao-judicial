package br.com.microservices.intimacaojudicial.dto;

import jakarta.validation.constraints.NotBlank;

public record ReuDTO( @NotBlank(message = "O nome do réu não pode estar em branco.") String nome) { }
