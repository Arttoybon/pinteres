package com.example.pinteres.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email {

	private String destinatario;

	private String asunto;

	private String mensaje;

	private String urlBtn;

}
