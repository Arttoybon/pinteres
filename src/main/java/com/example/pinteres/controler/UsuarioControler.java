package com.example.pinteres.controler;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pinteres.entity.Usuario;
import com.example.pinteres.service.UsuarioService;

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

    @GetMapping("/cambiar-password")
    public String mostrarPantallaCambio(
            @RequestParam("token") String tokenRecibido, 
            @RequestParam("u") String nombreHasheadoRecibido, 
            Model model) {
        
        List<Usuario> todosLosUsuarios = usuarioService.listarTodos();
        Usuario usuarioEncontrado = null;

        for (Usuario u : todosLosUsuarios) {
            // Generamos el hash del nombre usando UTF_8 para evitar errores de encoding
            String hashNombreDB = org.springframework.util.DigestUtils.md5DigestAsHex(
                    u.getNombre().getBytes(java.nio.charset.StandardCharsets.UTF_8));
       
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
 
            usuarioService.actualizarContraseya(nombreUsuario,nuevaContrasenya);

        
        return "redirect:/?actualizado=true";
    }
    
}
