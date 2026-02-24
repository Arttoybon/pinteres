package com.example.pinteres.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.pinteres.entity.Email;
import com.example.pinteres.service.implement.EmailInterface;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailInterface {

	private final JavaMailSender javaMailSender;
	
	private final TemplateEngine templateEngine;

	public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {

		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
	}	
	
	@Override
	public void sendMail(Email email) throws MessagingException {
		try {
			MimeMessage  message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
			helper.setTo(email.getDestinatario());
			helper.setFrom("rosadelalbaxx@gmail.com");
			helper.setSubject(email.getAsunto());
			Context context = new Context();
			context.setVariable("mensaje",email.getMensaje());
			String contentHtml= templateEngine.process("email", context);
			
			helper.setText(contentHtml,true);
			javaMailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException("Error al enviar el correo: "+e.getMessage());
		}
	}
	
}
