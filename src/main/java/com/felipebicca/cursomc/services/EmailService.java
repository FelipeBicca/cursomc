package com.felipebicca.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.felipebicca.cursomc.domain.Cliente;
import com.felipebicca.cursomc.domain.Pedido;


public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido ped);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHTMLEmail(Pedido ped);
	
	void sendHTMLEmail(MimeMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
