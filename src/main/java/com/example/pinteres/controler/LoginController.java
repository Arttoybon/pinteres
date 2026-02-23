package com.example.pinteres.controler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pinteres.entity.Usuario;
import com.example.pinteres.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/login")
	public String login(@RequestParam String nombre, @RequestParam String contrasenya, HttpSession session) {
		Optional<Usuario> usuario = usuarioRepository.findById(nombre);

		if (usuario.isPresent() && usuario.get().getContrasenya().equals(contrasenya)) {
			session.setAttribute("usuarioLogueado", usuario.get());
			return "redirect:/home";
		}
		return "redirect:/?error=true";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/recuperar-password")
	public String recuperarPassword(@RequestParam("usuario") String nombre, Model model) {
	    Optional<Usuario> usuario = usuarioRepository.findById(nombre);

	    if (usuario.isPresent()) {
	        // Redirigimos a la ruta que carga el formulario de cambio
	        // Usamos el nombre como identificador ya que tu repositorio lo usa así
	        return "redirect:/usuario/cambiar-password?id=" + usuario.get().getNombre();
	    } else {
	        // Importante: Para que el error se vea en el index tras un redirect, 
	        // suele ser mejor usar redirect:/?errorUsuario=true
	        return "redirect:/?noexiste=true";
	    }
	}
}