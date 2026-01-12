package com.epiis.app;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.epiis.app.auxobject.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	/**
	 * Genera un token JWT básico con los detalles del usuario.
	 *
	 * @param userDetails Mapa con detalles del usuario (ej. username).
	 * @return Token JWT generado.
	 */
	public String generateToken(Map<String, String> userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	/**
	 * Genera un token JWT con claims adicionales y tiempo de expiración.
	 * Utiliza la clave secreta definida en JwtProperties.
	 *
	 * @param extraClaims Mapa de claims adicionales.
	 * @param userDetails Mapa con detalles del usuario.
	 * @return Token JWT firmado.
	 */
	public String generateToken(Map<String, Object> extraClaims, Map<String, String> userDetails) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.get("userName"))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JwtProperties.timeAuthMs))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * Valida la firma y estructura del token JWT.
	 *
	 * @param token Token JWT a validar.
	 * @return true si el token es válido, false si ha expirado o es incorrecto.
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(getSignInKey())
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Extrae el nombre de usuario (subject) del token.
	 *
	 * @param token Token JWT.
	 * @return Nombre de usuario.
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extrae un claim específico del token utilizando una función resolver.
	 *
	 * @param token Token JWT.
	 * @param claimsResolver Función para extraer el claim deseado.
	 * @return Valor del claim extraído.
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);

		return claimsResolver.apply(claims);
	}

	/**
	 * Extrae todos los claims (datos) del token.
	 *
	 * @param token Token JWT.
	 * @return Objeto Claims con todos los datos.
	 */
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	/**
	 * Verifica si el token es válido para un usuario específico y no ha expirado.
	 *
	 * @param token Token JWT.
	 * @param userDetails Detalles del usuario para comparar.
	 * @return true si es válido.
	 */
	public boolean isTokenValid(String token, Map<String, String> userDetails) {
		final String username = extractUsername(token);

		return (username.equals(userDetails.get("userName"))) && !isTokenExpired(token);
	}

	/**
	 * Verifica si el token ha expirado.
	 *
	 * @param token Token JWT.
	 * @return true si ha expirado.
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Extrae la fecha de expiración del token.
	 *
	 * @param token Token JWT.
	 * @return Fecha de expiración.
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Obtiene la clave de firma decodificada desde la configuración.
	 *
	 * @return Clave HMAC SHA.
	 */
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JwtProperties.secretKey);

		return Keys.hmacShaKeyFor(keyBytes);
	}
}