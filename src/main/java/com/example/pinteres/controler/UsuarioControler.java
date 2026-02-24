package com.example.pinteres.controler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pinteres.entity.Usuario;
import com.example.pinteres.service.UsuarioService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/usuario")
public class UsuarioControler {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public String registrarUsuario(@RequestParam String nombre, 
                                   @RequestParam String contrasenya, 
                                   @RequestParam String correo) { // Recibe el correo
        if (usuarioService.buscarPorNombre(nombre) != null || nombre.trim().equals("")) {
            return "redirect:/?existe=true";
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre);
        nuevo.setContrasenya(contrasenya);
        nuevo.setCorreo(correo);
        
        usuarioService.guardar(nuevo);
        return "redirect:/?registrado=true";
    }
    
    @PostMapping("/validar-recuperacion")
    public String validarRecuperacion(@RequestParam("usuario") String nombre, 
                                      RedirectAttributes redirectAttrs, 
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

 // Cambia Long id por String id
    @GetMapping("/cambiar-password")
    public String mostrarPantallaCambio(@RequestParam("id") String id, Model model) {
        // Buscamos al usuario para pasar sus datos al formulario si fuera necesario
        model.addAttribute("userId", id);
        return "formulario-cambio"; // Asegúrate de que este archivo exista en templates
    }
    
    @PostMapping("/actualizar-contraseya")
    public String actualizarContraseya(@RequestParam("idViejo") String nombreUsuario,
                                     @RequestParam("nuevaContrasenya") String nuevaContrasenya) {
 
            usuarioService.actualizarContraseya(nombreUsuario,nuevaContrasenya);

        
        return "redirect:/?actualizado=true";
    }
    
}
