package com.example.pinteres.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pinteres.entity.Usuario;
import com.example.pinteres.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuResp;

	@Autowired
	private BCryptPasswordEncoder encoder;

	public UsuarioService(UsuarioRepository usuResp) {
		this.usuResp = usuResp;

	}

	// CREATE
	public void guardar(Usuario usuario) {
		// Hasheamos la contraseña antes de guardar en la DB
		String passwordHasheada = encoder.encode(usuario.getContrasenya());
		usuario.setContrasenya(passwordHasheada);

		usuResp.save(usuario);
	}

	// READ ALL
	public List<Usuario> listarTodos() {
		return usuResp.findAll();
	}

	// READ BY ID
	public Usuario buscarPorNombre(String nombre) {
		return usuResp.findById(nombre).orElse(null);
	}

	// UPDATE
	public Usuario actualizar(Usuario usu) {
		Usuario u = buscarPorNombre(usu.getNombre());

		u.setCorreo(usu.getCorreo());
		u.setContrasenya(usu.getContrasenya());

		return usuResp.save(u);
	}

	// UPDATE
	public Usuario actualizarContraseya(String nombre, String Contraseya) {
		Usuario u = buscarPorNombre(nombre);
		String passwordHasheada = encoder.encode(Contraseya);

		u.setContrasenya(passwordHasheada);

		return usuResp.save(u);
	}

	// DELETE
	public void borrar(String nombre) {
		usuResp.deleteById(nombre);
	}

}
