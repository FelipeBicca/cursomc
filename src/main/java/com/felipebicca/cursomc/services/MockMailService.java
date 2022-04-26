package com.felipebicca.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockMailService extends AbstractMailService {

	private static final Logger LOG = LoggerFactory.getLogger(MockMailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando envio de e-mail....");
		LOG.info(msg.toString());
		LOG.info("E-Mail enviado!");
	}

	@Override
	public void sendHTMLEmail(MimeMessage msg) {
		LOG.info("Simulando envio de e-mail HTML....");
		LOG.info(msg.toString());
		LOG.info("E-Mail enviado!");
		
	}

}
