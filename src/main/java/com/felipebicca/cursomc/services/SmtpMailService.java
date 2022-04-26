package com.felipebicca.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpMailService extends AbstractMailService {

	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private JavaMailSender javaMailSender;

	private static final Logger LOG = LoggerFactory.getLogger(MockMailService.class);

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando e-mail....");
		mailSender.send(msg);
		LOG.info("E-Mail enviado!");

	}

	@Override
	public void sendHTMLEmail(MimeMessage msg) {
		LOG.info("Enviando e-mail....");
		javaMailSender.send(msg);
		LOG.info("E-Mail enviado!");
		
	}

}
