package pe.upc.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	/**
	 *  Implementación de Spring Security que encripta passwords con el algoritmo Bcrypt
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
	   return new BCryptPasswordEncoder();
    }
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select username, password, estatus from Usuarios where username=?")
		.authoritiesByUsernameQuery("select u.username, p.perfil from usuario_perfil up " + 
			"inner join Usuarios u on u.id = up.id_usuario " + 
			"inner join Perfiles p on p.id = up.id_perfil " + 
			"where u.username = ?");
	}

	/**
	 * Personalizamos el Acceso a las URLs de la aplicación
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()             	
    	// Los recursos estáticos no requieren autenticación
        .antMatchers(
                "/bootstrap/**",                        
                "/images/**",
                "/tinymce/**",
                "/logos/**").permitAll()        
        // Las vistas públicas u otras específicas no requieren autenticación
        .antMatchers( "/").permitAll()        
        // Permisos de URLs por ROLES
        //.antMatchers("/**").hasAnyAuthority("ADMINISTRADOR")
        .antMatchers("/edit/**").hasAnyAuthority("ADMINISTRADOR")
        .antMatchers("/updateform/**").hasAnyAuthority("ADMINISTRADOR")
        .antMatchers("/delete/**").hasAnyAuthority("ADMINISTRADOR")
        .antMatchers("/update-user/**").hasAnyAuthority("ADMINISTRADOR")
        .antMatchers("/addUser/**").hasAnyAuthority("USUARIO", "ADMINISTRADOR")
        .antMatchers("/add-user").hasAnyAuthority("USUARIO", "ADMINISTRADOR")
        .antMatchers("/adduserform/**").hasAnyAuthority("USUARIO", "ADMINISTRADOR")
        
        // El resto de URLs de la Aplicación requieren autenticación
        .anyRequest().authenticated()
        // El formulario de Login no requiere autenticacion
        .and().formLogin().loginPage("/login").permitAll()        
        .and().logout().permitAll().and()
		.exceptionHandling().accessDeniedPage("/error");;
    }
	
}