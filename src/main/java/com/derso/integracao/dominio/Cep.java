package com.derso.integracao.dominio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Cep(
		String logradouro,
		String complemento,
		String bairro,
		String localidade) {

}
