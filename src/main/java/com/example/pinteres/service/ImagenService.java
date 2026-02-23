package com.example.pinteres.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pinteres.entity.Imagen;
import com.example.pinteres.entity.Usuario;
import com.example.pinteres.repository.ImagenRepository;
import com.example.pinteres.repository.UsuarioRepository;

@Service
public class ImagenService {

	private final ImagenRepository imgRep;
	private final UsuarioRepository usuResp;

	public ImagenService(ImagenRepository imgRep, UsuarioRepository usuResp) {
		this.imgRep = imgRep;
		this.usuResp = usuResp;
	}

	public Imagen guardar(Imagen imagen, String nombreUsuario) {
		Usuario usuario = usuResp.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Usuario no existe"));

		imagen.setUsuario(usuario);

		return imgRep.save(imagen);
	}

	public List<Imagen> listarTodas() {
		return imgRep.findAll();
	}

	public Imagen buscarPorId(Long id) {
		return imgRep.findById(id).orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
	}

	public List<Imagen> imagenesDeUsuario(String nombre) {
		return imgRep.findByUsuarioNombre(nombre);
	}

	public Imagen actualizar(Long id, Imagen datos) {
		Imagen img = buscarPorId(id);

		img.setTitulo(datos.getTitulo());
		img.setEnlace(datos.getEnlace());

		return imgRep.save(img);
	}

	public void borrar(Long id) {
		imgRep.deleteById(id);
	}

}
