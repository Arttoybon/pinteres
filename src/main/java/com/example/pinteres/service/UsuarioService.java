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
	    Usuario u = usuResp.findById(nombreAntiguo)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    if (!nombreAntiguo.equals(usuNuevos.getNombre())) {
	        // 1. Comprobar si el NUEVO nombre ya existe para evitar otro error
	        if(usuResp.existsById(usuNuevos.getNombre())) {
	            throw new RuntimeException("El nombre de usuario ya existe");
	        }

	        u.setCorreo("temp_" + u.getNombre() + "@mail.com");
	        usuResp.saveAndFlush(u); 

	        // 3. Crear el nuevo usuario
	        Usuario nuevoUsuario = new Usuario();
	        nuevoUsuario.setNombre(usuNuevos.getNombre());
	        nuevoUsuario.setCorreo(usuNuevos.getCorreo()); // Aquí ya no chocará
	        
	        if (nuevaPass != null && !nuevaPass.trim().isEmpty()) {
	            nuevoUsuario.setContrasenya(encoder.encode(nuevaPass));
	        } else {
	            nuevoUsuario.setContrasenya(u.getContrasenya());
	        }

	        // 4. Mover fotos
	        if (u.getGaleria() != null) {
	            for (Imagen img : u.getGaleria()) {
	                img.setUsuario(nuevoUsuario);
	            }
	            nuevoUsuario.setGaleria(u.getGaleria());
	        }

	        // 5. Guardar el nuevo y borrar el viejo definitivamente
	        Usuario guardado = usuResp.save(nuevoUsuario);
	        usuResp.delete(u);
	        
	        return guardado;
	    } else {
	        // Si el nombre es igual, solo actualizamos los campos normales
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
	@Transactional
	public void eliminarCuenta(String nombre) {
	    Usuario u = buscarPorNombre(nombre);
	    // JPA se encargará de borrar las imágenes si tienes CascadeType.ALL
	    // o de ponerlas a null si así lo configuraste.
	    usuResp.delete(u);
	}

}
