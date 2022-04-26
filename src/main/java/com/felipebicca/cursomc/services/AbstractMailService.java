package com.felipebicca.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.felipebicca.cursomc.domain.Pedido;

public abstract class AbstractMailService implements EmailService{
	
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido ped) {
		SimpleMailMessage sm = prepareSimpleMessageFromPedido(ped);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMessageFromPedido(Pedido ped) {
		SimpleMailMessage sm = new SimpleMailMessage();
		
		sm.setTo(ped.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: " + ped.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(ped.toString());
		return sm;
	}

}
