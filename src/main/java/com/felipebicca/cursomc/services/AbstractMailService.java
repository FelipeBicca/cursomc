package com.felipebicca.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.felipebicca.cursomc.domain.Pedido;

public abstract class AbstractMailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;

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

	protected String htmlFromTemplatePedido(Pedido ped) {
		Context context = new Context();
		context.setVariable("pedido", ped);

		return templateEngine.process("email/confirmacaoPedido", context);
	}

	@Override
	public void sendOrderConfirmationHTMLEmail(Pedido ped) {
		MimeMessage mm;
		try {
			mm = prepareMimeMessageFromPedido(ped);
			sendHTMLEmail(mm);
		} catch (MessagingException e) {
			sendOrderConfirmationEmail(ped);
		}
		
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido ped) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setTo(ped.getCliente().getEmail());
		mimeMessageHelper.setFrom(sender);
		mimeMessageHelper.setSubject("Pedido Confirmado! Código: " + ped.getId());
		mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
		mimeMessageHelper.setText(htmlFromTemplatePedido(ped), true);
		return mimeMessage;
	}

}
