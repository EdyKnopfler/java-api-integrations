package com.derso.integracao;

import java.io.IOException;

import com.derso.integracao.dominio.Cep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CepEmJson {

	public static void main(String[] args) {
		Cep cep = new Cep("Rua da Amargura", "51", "Cachaceiros", "Pirassununga");
		comGson(cep);
		comJackson(cep);
	}
	
	private static void comGson(Cep cep) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(cep);
		System.out.println("Com Gson: " + json);
	}

	private static void comJackson(Cep cep) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			//objectMapper.writeValue(new File("cep.json"), cep);
			String json = objectMapper.writeValueAsString(cep);
			System.out.println("Com Jackson: " + json);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
