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

	@PostMapping("/actualizar-contraseya")
	public String actualizarContraseya(@RequestParam("idViejo") String nombreUsuario,
			@RequestParam("nuevaContrasenya") String nuevaContrasenya) {

		usuarioService.actualizarContraseya(nombreUsuario, nuevaContrasenya);

		return "redirect:/?actualizado=true";
	}

	@PostMapping("/actualizar-perfil")
	public String procesarActualizacion(@RequestParam String nuevoNombre,
	                                   @RequestParam String correo,
	                                   @RequestParam(required = false) String nuevaPass,
	                                   HttpSession session) {
	    
	    Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
	    if (logueado == null) return "redirect:/";

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
	
	@GetMapping("/mi-perfil")
    public String miPerfil(Model model, HttpSession session) {
        // 1. Obtener el usuario de la sesión
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }

        // 2. Usar el nombre exacto del método de tu ImagenService
        // Tu servicio recibe el nombre (String), no el objeto Usuario completo
        List<Imagen> misPublicaciones = imagenService.imagenesDeUsuario(usuarioLogueado.getNombre());

        // 3. Añadir todo al modelo usando la variable 'model'
        model.addAttribute("usuario", usuarioLogueado);
        model.addAttribute("publicaciones", misPublicaciones);
        model.addAttribute("esMiPerfil", true);
        
        // ESTO ARREGLA EL ERROR "null context object" en perfil-usuario.html
        model.addAttribute("imagenService", imagenService); 

        return "perfil-usuario";
    }

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
