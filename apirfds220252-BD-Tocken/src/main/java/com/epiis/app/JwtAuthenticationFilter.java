package com.epiis.app;

// JWT Authentication Filter
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
/**
 * Filtro de autenticación JWT que se ejecuta en cada petición.
 * Intercepta las solicitudes HTTP para validar la presencia y validez del token JWT.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	public JwtAuthenticationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	/**
	 * Lógica principal del filtro.
	 * 1. Verifica si la ruta es pública.
	 * 2. Extrae el token del header Authorization.
	 * 3. Valida el token usando JwtService.
	 * 4. Si es válido, establece la autenticación en el SecurityContext.
	 */
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String path = request.getServletPath();

		// ==============================
		// RUTAS PÚBLICAS: NO FILTRAR
		// ==============================
		if (path.startsWith("/api/auth/")
				|| path.startsWith("/v3/api-docs")
				|| path.startsWith("/swagger-ui")) {

			filterChain.doFilter(request, response);
			return;
		}

		// ==============================
		// LOGS SOLO PARA RUTAS PROTEGIDAS
		// ==============================
		System.out.println("=== JWT FILTER ===");
		System.out.println("Path: " + path);

		final String authHeader = request.getHeader("Authorization");
		System.out.println("Auth Header: " + authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			System.out.println("No Bearer token found");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			System.out
					.println("JWT Token (primeros 20 chars): " + jwt.substring(0, Math.min(20, jwt.length())) + "...");

			final String username = jwtService.extractUsername(jwt);
			System.out.println("Username from token: " + username);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				Map<String, String> userData = Map.of("userName", username);

				if (jwtService.isTokenValid(jwt, userData)) {
					System.out.println("Token válido para: " + username);

					List<SimpleGrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userData,
							null, authorities);

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);

					System.out.println("Autenticación establecida en SecurityContext");
				} else {
					System.out.println("Token inválido");
				}
			}

		} catch (Exception e) {
			System.err.println("Error en JWT filter: " + e.getMessage());
		}

		filterChain.doFilter(request, response);
	}
}