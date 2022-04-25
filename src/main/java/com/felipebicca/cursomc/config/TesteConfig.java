package com.felipebicca.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.felipebicca.cursomc.services.DBService;
import com.felipebicca.cursomc.services.EmailService;
import com.felipebicca.cursomc.services.MockMailService;

@Configuration
@Profile("test")
public class TesteConfig {
	@Autowired
	private DBService dbService;
	
	@Bean
	public Boolean instantiateDatabase() throws ParseException {	
		dbService.instantiateTestDatabase();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new MockMailService();
	}
}
