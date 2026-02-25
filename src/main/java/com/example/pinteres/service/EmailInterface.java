package com.example.pinteres.service;

import com.example.pinteres.entity.Email;

import jakarta.mail.MessagingException;

public interface EmailInterface {

	
	public void sendMail(Email email) throws MessagingException;
}
