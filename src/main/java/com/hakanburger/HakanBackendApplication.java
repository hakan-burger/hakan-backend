package com.hakanburger;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HakanBackendApplication {

	public static void main(String[] args) {
		// Hora de Perú aunque el servidor (Render) esté en UTC
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
		SpringApplication.run(HakanBackendApplication.class, args);
	}

}
