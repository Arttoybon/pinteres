package com.example.pinteres.controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pinteres.entity.Imagen;
import com.example.pinteres.entity.Usuario;
import com.example.pinteres.service.ImagenService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/gestion")
public class GestionControler {

	@Autowired
	private ImagenService imagenService;

	/**
	 * 
	 * Pinta la vista de los pines del usuario.
	 * 
	 * @param id      Si se proporciona un id de la imagen, se preselecciona.
	 * @param model   Datos para pintar la plantilla de mis-pines.
	 * @param session Datos de sesión del usuario logeado.
	 * @return La plantilla a pintar. Si hay algún error, se mostrará alerta en la
	 *         página.
	 */
	@GetMapping("/mis-pines")
	public String vistaGestion(@RequestParam(required = false) Long id, Model model, HttpSession session) {
		try {
			Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
			if (user == null) {
				return "redirect:/";
			}

			List<Imagen> misImagenes = imagenService.imagenesDeUsuario(user.getNombre());
			model.addAttribute("imagenes", misImagenes);

			Imagen seleccionada = null;

			if (id != null) {
				seleccionada = imagenService.buscarPorId(id);
			}

			// Si sigue siendo null (porque no se pasó ID o el ID no existe),
			// pero hay imágenes en la lista, agarramos la primera.
			if (seleccionada == null && !misImagenes.isEmpty()) {
				seleccionada = misImagenes.get(0);
			}

			model.addAttribute("imgSeleccionada", seleccionada);

			model.addAttribute("paginaActiva", "pines");

			return "mis-pines";
		} catch (Exception e) {
			return "redirect:/gestion/mis-pines?erroru=true";
		}
	}

	/**
	 * Edita todos los atributos del pin
	 * 
	 * @param id      Identificador de la imagen
	 * @param titulo  Titulo de la imagen
	 * @param enlace  Enlace de la imagen
	 * @param session Datos de sesión del usuario logeado.
	 * @return La plantilla a pintar. Si hay algún error, se mostrará alerta en la
	 *         página.
	 */
	@PostMapping("/editar")
	public String editar(@RequestParam Long id, @RequestParam String titulo, @RequestParam String enlace,
			HttpSession session) {
		try {
			Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
			Imagen img = imagenService.buscarPorId(id);

			// Verificamos que el usuario es el dueño de la imagen
			if (user != null && img != null && img.getUsuario().getNombre().equals(user.getNombre())) {
				// Actualizamos los valores del objeto
				img.setTitulo(titulo);
				img.setEnlace(enlace);

				imagenService.actualizar(id, img);
			}

			// Redirigimos de vuelta a la gestión manteniendo la imagen seleccionada
			return "redirect:/gestion/mis-pines?id=" + id;
		} catch (Exception e) {
			return "redirect:/gestion/mis-pines?erroru=true";
		}
	}

	/**
	 * Borra las imagenes por su identificador
	 * 
	 * @param id      Identificador de la imagen
	 * @param session Datos de sesión del usuario logeado.
	 * @return La plantilla a pintar. Si hay algún error, se mostrará alerta en la
	 *         página.
	 */
	@GetMapping("/borrar/{id}")
	public String borrar(@PathVariable Long id, HttpSession session) {
		try {
			Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
			if (user == null) {
				return "redirect:/";
			}

			Imagen selectedImg = imagenService.buscarPorId(id);

			// Verificamos que la imagen existe y que el nombre del dueño coincide
			if (selectedImg == null || !user.getNombre().equals(selectedImg.getUsuario().getNombre())) {
				return "redirect:/gestion/mis-pines?erroru=true";
			}

			imagenService.borrar(id);
			return "redirect:/gestion/mis-pines";
		} catch (Exception e) {
			return "redirect:/gestion/mis-pines?erroru=true";
		}

	}
}