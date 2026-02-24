package com.example.pinteres.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {
	@Value("${email.username}")
	private String email;
	
	@Value("${email.password}")
	private String password;
	
	private Properties getMailProperties() {
		Properties propertis = new Properties();
		propertis.put("mail.smtp.aut","true");
		propertis.put("mail.smtp.starttls.enable","true");
		propertis.put("mail.smtp.host","smtp.gmail.com");
		propertis.put("mail.smtp.port","587");
		return propertis;
	}
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender= new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(getMailProperties());
		mailSender.setUsername(email);
		mailSender.setPassword(password);
		return mailSender;
	}
	
	@Bean
	public ResourceLoader resourceLoader() {
		return new DefaultResourceLoader();
	}
}
