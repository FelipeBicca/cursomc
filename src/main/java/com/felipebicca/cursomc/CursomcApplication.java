package com.felipebicca.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.felipebicca.cursomc.services.S3Services;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	
	@Autowired
	S3Services s3Services;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		s3Services.uploadFile("C:\\Users\\Confidere\\Pictures\\Minhas Fotos\\PETS\\Pandora\\IMG_4391.JPG");
		
	}
}
