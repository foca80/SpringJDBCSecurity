package pe.upc.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import pe.upc.entities.Perfil;
import pe.upc.entities.Usuario;
import pe.upc.services.UsuarioService;

@Controller
public class UsuarioController {
	@Autowired
	private UsuarioService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String home(Model model, HttpSession session) {
		
		model.addAttribute("nombre", "Carlos");
		model.addAttribute("users", userService.list());
		session.setAttribute("tienda", "Ripley");
		return "home";
	}

	/**
	 * Método que muestra el formulario de login personalizado.
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String mostrarLogin() {
	    return "formLogin";
	}

	/**
	 * Método personalizado para cerrar la sesión del usuario
	 * @param request
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		try {
			//objeto de Spring especial para cerrar sesión
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		//cerrando sesión
		logoutHandler.logout(request, null, null);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return "redirect:/";
		}
		return "redirect:/";
	}
	
	@GetMapping("/addUser")
	public String add(Model model, HttpServletRequest request) {
		
		model.addAttribute("user", new Usuario());
		// le pasa un user sin datos al form en blanco en th:object="${user}" y así al
		// llenar el form
		// se pueda reconocer los datos que ingresa en adduserform - binding
		return "add-user";
	}

	@PostMapping("/adduserform")
	public String addUser(@Valid Usuario user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-user";
		}
		// Recuperamos el password en texto plano
		String pwdPlano = user.getPassword();
		System.out.println("Texto: claro:" + pwdPlano);
		// Encriptamos el pwd BCryptPasswordEncoder
		String pwdEncriptado = passwordEncoder.encode(pwdPlano);
		// Hacemos un set al atributo password (ya viene encriptado)
		user.setPassword(pwdEncriptado);
		user.setEstatus(1); // Activado por defecto administrador
		user.setFechaRegistro(new Date()); // Fecha de Registro, la fecha actual del servidor
		// Creamos el Perfil que le asignaremos al usuario nuevo
		Perfil perfil = new Perfil();
		perfil.setId(1); // Perfil administrador
		user.agregar(perfil);

		userService.insert(user);
		model.addAttribute("users", userService.list());
		return "home";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Usuario user = userService.search(id);
		user.setPassword("");
		model.addAttribute("user", user);
		return "update-user";
	}

	@PostMapping("/updateform/{id}")
	public String updateUser(@PathVariable("id") int id, @Valid Usuario user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			user.setId(id);
			return "update-user";
		}
		// Recuperamos el password en texto plano
		String pwdPlano = user.getPassword();
		// Encriptamos el pwd BCryptPasswordEncoder
		String pwdEncriptado = passwordEncoder.encode(pwdPlano);
		// Hacemos un set al atributo password (ya viene encriptado)
		user.setPassword(pwdEncriptado);
		user.setEstatus(1); // Activado por defecto administrador
		user.setFechaRegistro(new Date()); // Fecha de Registro, la fecha actual del servidor
		// Creamos el Perfil que le asignaremos al usuario nuevo
		Perfil perfil = new Perfil();
		perfil.setId(1); // Perfil USUARIO
		user.agregar(perfil);

		userService.insert(user);
		model.addAttribute("users", userService.list());
		return "home";
	}

    @GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") int id, Model model) {
		Usuario user = userService.search(id);
		userService.delete(user);
		model.addAttribute("users", userService.list());
		return "home";
	}

	@PostMapping("/insert/{idUsuario}/{idPerfil}")
	public String asignar(@PathVariable("idUsuario") int idUsuario, @PathVariable("idPerfil") int idPerfil) {
		userService.insert(idUsuario, idPerfil);
		return "home";
	}
	@GetMapping("/error")
	public String error() {
		return "error";
	}

}
