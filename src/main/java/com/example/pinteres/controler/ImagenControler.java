package com.example.pinteres.controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pinteres.entity.Imagen;
import com.example.pinteres.entity.Usuario;
import com.example.pinteres.service.ImagenService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ImagenControler {

	@Autowired
	private final ImagenService imagenService;

	public ImagenControler(ImagenService imagenService) {
		this.imagenService = imagenService;
	}

	@GetMapping("/home")
	public String home(Model model, HttpSession session) {
	    if (session.getAttribute("usuarioLogueado") == null) {
	        return "redirect:/";
	    }
	    
	    model.addAttribute("imagenes", imagenService.listarTodas());
	    model.addAttribute("usuario", session.getAttribute("usuarioLogueado"));
	    model.addAttribute("imagenService", imagenService); 
	    
	    // Cambiamos "pagina" por "paginaActiva" para que coincida con el Navbar
	    model.addAttribute("paginaActiva", "home");
	    
	    return "home";
	}

	@PostMapping("/publicar")
	public String publicar(@RequestParam String titulo, @RequestParam String enlace, HttpSession session) {
		Usuario user = (Usuario) session.getAttribute("usuarioLogueado");

		Imagen nuevaImg = new Imagen();
		nuevaImg.setTitulo(titulo);
		nuevaImg.setEnlace(enlace);

		imagenService.guardar(nuevaImg, user.getNombre());

		return "redirect:/home";
	}

	@GetMapping("/borrar/{id}")
	public String eliminar(@PathVariable Long id) {
		imagenService.borrar(id);
		return "redirect:/";
	}

	@GetMapping("/mis-pines")
	public String vistaGestion(Model model, HttpSession session) {
	    Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
	    if (user == null) return "redirect:/";

	    List<Imagen> misImagenes = imagenService.imagenesDeUsuario(user.getNombre());
	    model.addAttribute("imagenes", misImagenes);
	    model.addAttribute("paginaActiva", "pines"); // <--- Identificador para Mis Pines

	    if (!misImagenes.isEmpty()) {
	        model.addAttribute("primeraImagen", misImagenes.get(0));
	    }

	    return "mis-pines";
	}

	// Añade estos métodos a ImagenControler.java

	@PostMapping("/like/{id}")
	@ResponseBody
	public boolean toggleLike(@PathVariable Long id, HttpSession session) {
	    Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
	    if (user == null) return false;
	    
	    // Modificaremos el service para que devuelva si tras el click está guardado o no
	    return imagenService.toggleLike(id, user.getNombre());
	}

	@GetMapping("/mis-guardados")
	public String vistaGuardados(Model model, HttpSession session) {
	    Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
	    if (user == null) return "redirect:/";

	    model.addAttribute("imagenes", imagenService.listarGuardadas(user.getNombre()));
	    model.addAttribute("tituloPagina", "Mis Favoritos");
	    model.addAttribute("imagenService", imagenService); 
	    model.addAttribute("paginaActiva", "guardados"); // <--- Identificador para Favoritos

	    return "home"; 
	}
}
