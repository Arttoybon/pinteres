package com.example.pinteres.controler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pinteres.entity.Email;
import com.example.pinteres.entity.Usuario;
import com.example.pinteres.repository.UsuarioRepository;
import com.example.pinteres.service.implement.EmailInterface;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@Autowired
	EmailInterface emailServ;
	
	@Autowired
    private BCryptPasswordEncoder encoder;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/login")
	public String login(@RequestParam String nombre, @RequestParam String contrasenya, HttpSession session) {
		Optional<Usuario> usuario = usuarioRepository.findById(nombre);
		
		if (usuario.isPresent() &&encoder.matches(contrasenya, usuario.get().getContrasenya())) {
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
	public String sendEmail(@RequestParam("usuario")String nombre ) {
		try {
			Optional<Usuario> usuario = usuarioRepository.findById(nombre);
			 if (usuario.isPresent()) {
				 Email emai = new Email(usuario.get().getCorreo(),"Recuperación de correo","Entra en el siguiente "
				 		+ "enlace para restablecer tu contraseña.",
				 		"http://localhost:8080/usuario/cambiar-password?id="+usuario.get().getNombre());
					emailServ.sendMail(emai);
			 }else {
			        // Importante: Para que el error se vea en el index tras un redirect, 
			        // suele ser mejor usar redirect:/?errorUsuario=true
			        return "redirect:/?noexiste=true";
			    }
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "redirect:/?recuperado=true";
	}
	
}