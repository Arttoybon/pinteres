package com.example.pinteres.controler;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pinteres.entity.Imagen;
import com.example.pinteres.entity.Usuario;
import com.example.pinteres.service.ImagenService;
import com.example.pinteres.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioControler {

	@Autowired
	private ImagenService imagenService;

	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Recibe los datos del usuario y los guarda en la base de datos
	 * 
	 * @param nombre nombre del usuario
	 * @param contrasenya contraseña del usuario
	 * @param correo correo del usuario
	 * @return Muestra una alerta de si se a completado con éxito o no
	 */
	@PostMapping("/registrar")
	public String registrarUsuario(@RequestParam String nombre, @RequestParam String contrasenya,
			@RequestParam String correo) { // Recibe el correo

		String nombreMin = (nombre != null) ? nombre.toLowerCase().trim() : "";
		if (usuarioService.buscarPorNombre(nombreMin) != null || nombreMin.equals("")) {
			return "redirect:/?existe=true";
		}

		Usuario nuevo = new Usuario();
		nuevo.setNombre(nombreMin);
		nuevo.setContrasenya(contrasenya);
		nuevo.setCorreo(correo);

		usuarioService.guardar(nuevo);
		return "redirect:/?registrado=true";
	}

	/**
	 * Valida si existe el usuario
	 * 
	 * @param nombre nombre del usuario
	 * @param redirectAttrs atributos de la redirección de la página, query.
	 * @param model contiene el erro en caso de usuario inexistente
	 * @return muestra la pantalla del index o la de cambiar contraseña
	 */
	@PostMapping("/validar-recuperacion")
	public String validarRecuperacion(@RequestParam("usuario") String nombre, RedirectAttributes redirectAttrs,
			Model model) {

		// 1. Buscamos al usuario en la base de datos
		Optional<Usuario> usuario = Optional.ofNullable(usuarioService.buscarPorNombre(nombre));

		if (usuario.isPresent()) {
			// 2. Si existe, lo enviamos al HTML de cambio de contraseña.
			// Pasamos el ID para saber a quién le vamos a cambiar la clave después.
			redirectAttrs.addAttribute("id", usuario.get().getNombre());
			return "redirect:/cambiar-password";
		} else {
			// 3. Si no existe, volvemos al index con el mensaje de error
			model.addAttribute("error", "Usuario no encontrado");
			return "index";
		}
	}

	/**
	 * Muestra la plantilla de cambio de contraseña
	 * 
	 * @param tokenRecibido contraseña y usuario encriptado
	 * @param nombreHasheadoRecibido nombre del usuario encriptado
	 * @param model
	 * @return id del usuario encontrado
	 */
	@GetMapping("/cambiar-password")
	public String mostrarPantallaCambio(@RequestParam("token") String tokenRecibido,
			@RequestParam("u") String nombreHasheadoRecibido, Model model) {

		List<Usuario> todosLosUsuarios = usuarioService.listarTodos();
		Usuario usuarioEncontrado = null;

		for (Usuario u : todosLosUsuarios) {
			// Generamos el hash del nombre usando UTF_8 para evitar errores de encoding
			String hashNombreDB = org.springframework.util.DigestUtils
					.md5DigestAsHex(u.getNombre().getBytes(java.nio.charset.StandardCharsets.UTF_8));

			if (hashNombreDB.equals(nombreHasheadoRecibido)) {
				usuarioEncontrado = u;
				break;
			}
		}

		if (usuarioEncontrado != null) {
			// Generamos el token de la contraseña usando UTF_8
			String tokenEsperado = org.springframework.util.DigestUtils.md5DigestAsHex(
					usuarioEncontrado.getContrasenya().getBytes(java.nio.charset.StandardCharsets.UTF_8));

			if (tokenEsperado.equals(tokenRecibido)) {
				model.addAttribute("userId", usuarioEncontrado.getNombre());
				return "formulario-cambio";
			}
		}

		return "redirect:/?enlaceInvalido=true";
	}

	/**
	 * Sutituye la contraseña antigua por la nueva
	 * 
	 * @param nombreUsuario nombre del usuario
	 * @param nuevaContrasenya nueva contraseña del usuario
	 * @return Muestra el login
	 */
	@PostMapping("/actualizar-contraseya")
	public String actualizarContraseya(@RequestParam("idViejo") String nombreUsuario,
			@RequestParam("nuevaContrasenya") String nuevaContrasenya) {

		usuarioService.actualizarContraseya(nombreUsuario, nuevaContrasenya);

		return "redirect:/?actualizado=true";
	}

	
	
	/**
	 * Recibe los datos del usuario actualizado
	 * 
	 * @param nuevoNombre nuevo nombre del usuario
	 * @param correo nuevo correo del usuario
	 * @param nuevaPass nueva contraseña del usuario
	 * @param session Datos de sesión del usuario logeado.
	 * @return Muestra el login en caso de error, si no un alert de que se han realizado.
	 */
	@PostMapping("/actualizar-perfil")
	public String procesarActualizacion(@RequestParam String nuevoNombre,
	                                   @RequestParam String correo,
	                                   @RequestParam(required = false) String nuevaPass,
	                                   HttpSession session) {

	    Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
	    if (logueado == null) {
			return "redirect:/";
		}

	    // Creamos un objeto temporal para el service
	    Usuario datosNuevos = new Usuario();
	    datosNuevos.setNombre(nuevoNombre.trim()); // Aquí sí es útil el trim para el valor
	    datosNuevos.setCorreo(correo.trim());

	    // Llamamos al service
	    Usuario actualizado = usuarioService.actualizar(logueado.getNombre(), datosNuevos, nuevaPass);

	    // Actualizamos la sesión para que la Navbar muestre el nuevo nombre
	    session.setAttribute("usuarioLogueado", actualizado);

	    return "redirect:/usuario/mi-perfil?exito=true";
	}

	/**
	 * Elimina la cuenta de la base de datos e invalida la sesión
	 * 
	 * @param session Datos de sesión del usuario logeado.
	 * @return Muestra el login
	 */
	@PostMapping("/eliminar-cuenta")
	public String eliminarCuenta(HttpSession session) {
	    Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");

	    if (logueado != null) {
	        usuarioService.eliminarCuenta(logueado.getNombre());
	        // Destruimos la sesión para que no pueda seguir navegando
	        session.invalidate();
	    }

	    return "redirect:/?cuentaEliminada=true";
	}

	/**
	 * Muestra el perfil del usuario logeado
	 * 
	 * @param model Contiene los datos del usuario y sus piblicaciones
	 * @param session Datos de sesión del usuario logeado.
	 * @return Muestra el perfil del usuario
	 */
	@GetMapping("/mi-perfil")
    public String miPerfil(Model model, HttpSession session) {
        // 1. Obtener el usuario de la sesión
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/";
        }


        List<Imagen> misPublicaciones = imagenService.imagenesDeUsuario(usuarioLogueado.getNombre());


        model.addAttribute("usuario", usuarioLogueado);
        model.addAttribute("publicaciones", misPublicaciones);
        model.addAttribute("esMiPerfil", true);

       
        model.addAttribute("imagenService", imagenService);

        return "perfil-usuario";
    }

	
    /**
     * Muestra el perfil del usuario al que pertenece la id
     * 
     * @param nombre del usuario
     * @param model Contiene los datos del usuario y sus piblicaciones, indicando que no es su perfil
     * @param session Datos de sesión del usuario logeado.
     * @return En caso de error te dirige al home
     */
    @GetMapping("/perfil/{nombre}")
    public String perfilPublico(@PathVariable String nombre, Model model, HttpSession session) {
        // 1. Buscar al usuario cuyo perfil queremos ver
        // (Asumo que tienes un método buscarPorNombre en tu UsuarioService)
        Usuario usuarioVer = usuarioService.buscarPorNombre(nombre);

        if (usuarioVer == null) {
            return "redirect:/home";
        }

        // 2. Obtener sus fotos
        List<Imagen> publicaciones = imagenService.imagenesDeUsuario(nombre);

        // 3. Pasar datos al modelo
        model.addAttribute("usuario", usuarioVer);
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("esMiPerfil", false);
        model.addAttribute("imagenService", imagenService); // Siempre pasarlo si el HTML lo usa

        return "perfil-usuario";
    }

}
