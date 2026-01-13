package com.epiis.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.epiis.app.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
/**
 * Configuración de seguridad de Spring Security.
 * Define la cadena de filtros de seguridad, reglas de CORS, y autorización de
 * rutas.
 */
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	/**
	 * Define el SecurityFilterChain.
	 * - Deshabilita CSRF (común en APIs REST stateless).
	 * - Configura CORS para permitir peticiones desde el frontend (Angular).
	 * - Define reglas de acceso por ruta (públicas vs autenticadas).
	 * - Configura la sesión como STATELESS (sin estado, usando tokens).
	 * - Agrega el filtro JWT antes del filtro de autenticación estándar.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http

				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(request -> {
					var corsConfig = new org.springframework.web.cors.CorsConfiguration();
					corsConfig.setAllowCredentials(true);
					corsConfig.addAllowedOrigin("http://localhost:4200"); // Angular Local
					corsConfig.addAllowedOrigin("https://gestion-de-personas-iota.vercel.app"); // Verel Prod
					corsConfig.addAllowedOrigin(
							"https://gestion-de-personas-git-main-carlos-daniels-proyectos-f76e513c.vercel.app"); // Vercel
																													// Preview
					corsConfig.addAllowedOrigin(
							"https://gestion-de-personas-b4bg6523d-carlos-daniels-proyectos-f76e513c.vercel.app"); // Vercel
																													// Other
					corsConfig.addAllowedHeader("*");
					corsConfig.addAllowedMethod("*");
					return corsConfig;
				}))
				.authorizeHttpRequests(auth -> auth
						// Endpoints públicos
						.requestMatchers("/api/auth/login").permitAll()
						.requestMatchers("/person/export/**").permitAll()
						// permitir swagger si lo usas
						.requestMatchers(
								"/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html")
						.permitAll()
						// Endpoints protegidos
						.requestMatchers("/person/insert", "/person/getall", "/person/count", "/person/search-name",
								"/person/search-surname", "/person/search-birth", "/person/page",
								"/person/exists-dni/**", "/person/get/**", "/person/find/**", "/person/delete/**")
						.authenticated()
						.anyRequest().authenticated())
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

				.build();
	}

}