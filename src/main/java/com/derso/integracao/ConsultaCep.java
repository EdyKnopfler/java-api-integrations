package com.derso.integracao;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.derso.integracao.dominio.Cep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConsultaCep {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Favor passar o CEP como argumento.");
			System.exit(1);
		}
		
		String cep = args[0].replaceAll("\\D+", "");
		String uri = "https://viacep.com.br/ws/" + cep + "/json/";
			
		HttpResponse<String> response = doRequest(uri);
		int statusCode = response.statusCode();
		
		if (statusCode != 200) {
			System.out.println("ViaCEP devolveu status: " + statusCode);
			System.exit(1);
		}
		
		String jsonContent = response.body();
		Cep withJackson = parseWithJackson(jsonContent);
		Cep withGson = parseWithGson(jsonContent);
		
		String tranqueira = """
			{
				"localidade": "São Paulo",
				"preco": 100.0
			}
		""";
		
		System.out.println("Resposta do ViaCEP: " + response.body());
		System.out.println("Com Jackson: " + withJackson);
		System.out.println("Com Gson: " + withGson);
		System.out.println("Tranqueira com Jackson: " + parseWithJackson(tranqueira));
		System.out.println("Tranqueira com Gson: " + parseWithGson(tranqueira));
	}

	@SuppressWarnings("unchecked")
	private static HttpResponse<String> doRequest(String uri) {
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(uri))
			.build();
			
		try {
			return client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			return (HttpResponse<String>) nullAfterRegisterError(
					"Erro ao realizar a requisição", e);
		}
	}
	
	// Mais burocrático
	// - Exige tratamento de erro e decidir o que fazer com atributos
	//   sobrando ou faltando no JSON
	// - Ver anotação na classe Cep 
	private static Cep parseWithJackson(String jsonContent) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			// Se fosse um array: Cep[].class
			return objectMapper.readValue(jsonContent, Cep.class);
		} catch (JsonProcessingException e) {
			return (Cep) nullAfterRegisterError("Erro ao parsear com jackson", e);
		}
	}
	
	private static Cep parseWithGson(String jsonContent) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(jsonContent, Cep.class);
	}
	
	private static Object nullAfterRegisterError(String msg, Exception e) {
		System.out.println(msg);
		e.printStackTrace();
		System.exit(1);
		return null;
	}

}
