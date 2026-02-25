package com.example.pinteres.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pinteres.entity.Imagen;
import com.example.pinteres.entity.Usuario;
import com.example.pinteres.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

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
	@Transactional
	public Usuario actualizar(String nombreAntiguo, Usuario usuNuevos, String nuevaPass) {
	    // 1. Buscamos el original (usamos el repo directamente para asegurar que está en el contexto)
	    Usuario u = usuResp.findById(nombreAntiguo)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    // 2. Si el nombre HA CAMBIADO
	    if (!nombreAntiguo.equals(usuNuevos.getNombre())) {
	        Usuario nuevoUsuario = new Usuario();
	        
	        // ASIGNAMOS EL ID MANUALMENTE ANTES DE NADA
	        nuevoUsuario.setNombre(usuNuevos.getNombre()); 
	        nuevoUsuario.setCorreo(usuNuevos.getCorreo());
	        
	        // Gestionar contraseña
	        if (nuevaPass != null && !nuevaPass.trim().isEmpty()) {
	            nuevoUsuario.setContrasenya(encoder.encode(nuevaPass));
	        } else {
	            nuevoUsuario.setContrasenya(u.getContrasenya());
	        }

	        // Mover galería: Actualizamos la relación hijo -> padre
	        if (u.getGaleria() != null && !u.getGaleria().isEmpty()) {
	            for (Imagen img : u.getGaleria()) {
	                img.setUsuario(nuevoUsuario);
	            }
	            nuevoUsuario.setGaleria(u.getGaleria());
	        }

	        // OPERACIÓN CRÍTICA:
	        usuResp.save(nuevoUsuario); // Guardamos el nuevo primero
	        usuResp.flush();            // Forzamos a Hibernate a escribirlo YA
	        usuResp.delete(u);          // Ahora borramos el viejo con seguridad
	        
	        return nuevoUsuario;
	    } else {
	        // 3. Si el nombre es igual, actualización estándar
	        u.setCorreo(usuNuevos.getCorreo());
	        if (nuevaPass != null && !nuevaPass.trim().isEmpty()) {
	            u.setContrasenya(encoder.encode(nuevaPass));
	        }
	        return usuResp.save(u);
	    }
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
