package com.example.pinteres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Imagenes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Imagen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;

	@Column( length = 1000)
    private String enlace;

	@ManyToOne
	@JoinColumn(name = "usuario_nombre")
	private Usuario usuario;

}
