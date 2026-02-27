package com.example.pinteres.controler;

import java.nio.charset.StandardCharsets;
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
import com.example.pinteres.service.EmailInterface;

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

	/**
	 * Plantilla de registro
	 * 
	 * @return A si mismo
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * Accion de iniciar sesion. Si se completa te lleva al home si no da error.
	 * 
	 * @param nombre nombre del usuario
	 * @param contrasenya contraseña del usuario
	 * @param session Datos de sesión del usuario logeado.
	 * @return La plantilla a pintar. Si hay algún error, se mostrará alerta en la
	 *         página.
	 */
	@PostMapping("/login")
	public String login(@RequestParam String nombre, @RequestParam String contrasenya, HttpSession session) {
		String nombreMin = (nombre != null) ? nombre.toLowerCase().trim() : "";
		Optional<Usuario> usuario = usuarioRepository.findById(nombreMin);

		if (usuario.isPresent() && encoder.matches(contrasenya, usuario.get().getContrasenya())) {
			session.setAttribute("usuarioLogueado", usuario.get());
			return "redirect:/home";
		}
		return "redirect:/?error=true";
	}

	/**
	 * Cierra la sesión en cualquier momento
	 * 
	 * @param session Datos de sesión del usuario logeado.
	 * @return Plantilla de login
	 */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	/**
	 * Función de recuperar contraseña y enviar un correo de recuperación de esta al usuario
	 * 
	 * @param nombre del usuario
	 * @return Si hay algún error, se mostrará alerta en la página y si se completa con éxito.
	 */
	@PostMapping("/recuperar-password")
	public String sendEmail(@RequestParam("usuario") String nombre) {
		try {
			String nombreMin = (nombre != null) ? nombre.toLowerCase().trim() : "";
			Optional<Usuario> usuarioOpt = usuarioRepository.findById(nombreMin);

			if (usuarioOpt.isPresent()) {
				Usuario usuario = usuarioOpt.get();

				// 1. Obtenemos el hash de BCrypt que ya está en la base de datos
				String hashActual = usuario.getContrasenya();
				// String usuarioHasheada = encoder.encode(usuario.getNombre());
				// 2. Lo convertimos a Hexadecimal para que la URL sea segura (sin $, / ni .)
				String tokenHex = org.springframework.util.DigestUtils
						.md5DigestAsHex(hashActual.getBytes(StandardCharsets.UTF_8));
				String tokenHexu = org.springframework.util.DigestUtils
						.md5DigestAsHex(usuario.getNombre().getBytes(StandardCharsets.UTF_8));
				// 3. Construimos la nueva URL con el token y el nombre
				String urlConToken = "https://unopportunely-cindery-merna.ngrok-free.dev/usuario/cambiar-password"
						+ "?token=" + tokenHex + "&u=" + tokenHexu;

				// 4. Configuramos y enviamos el email
				Email emai = new Email(usuario.getCorreo(), "Recuperación de contraseña",
						"Has solicitado restablecer tu contraseña. Haz clic en el botón para continuar. "
								+ "Este enlace solo funcionará una vez.",
						urlConToken);

				emailServ.sendMail(emai);
				return "redirect:/?recuperado=true";

			} else {
				return "redirect:/?noexiste=true";
			}

		} catch (MessagingException e) {
			e.printStackTrace();
			return "redirect:/?error=true";
		}
	}

}