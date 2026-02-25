package com.example.pinteres.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

	@Id
	private String nombre;

	@Column(name = "correo", unique = true, nullable = false)
	private String correo;

	@Column(nullable = false)
	private String contrasenya;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Imagen> galeria = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
	    name = "usuario_favoritos",
	    joinColumns = @JoinColumn(name = "usuario_nombre"),
	    inverseJoinColumns = @JoinColumn(name = "imagen_id")
	)
	private List<Imagen> guardados = new ArrayList<>();
}
