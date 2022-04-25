package com.felipebicca.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.felipebicca.cursomc.domain.Pedido;


public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido ped);
	
	void sendEmail(SimpleMailMessage msg);
}
