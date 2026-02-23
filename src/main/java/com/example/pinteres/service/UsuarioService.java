package com.example.pinteres.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pinteres.entity.Usuario;
import com.example.pinteres.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuResp;

	public UsuarioService(UsuarioRepository usuResp) {
		this.usuResp = usuResp;

	}

	// CREATE
	public Usuario guardar(Usuario usuario) {
		return usuResp.save(usuario);
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
	public Usuario actualizar(String nombre, Usuario datos) {
		Usuario u = buscarPorNombre(nombre);

		u.setCorreo(datos.getCorreo());
		u.setContrasenya(datos.getContrasenya());

		return usuResp.save(u);
	}

	// DELETE
	public void borrar(String nombre) {
		usuResp.deleteById(nombre);
	}

}
