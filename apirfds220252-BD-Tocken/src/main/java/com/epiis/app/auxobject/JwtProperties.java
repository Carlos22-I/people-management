package com.epiis.app.auxobject;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
/**
 * Clase de configuración para propiedades JWT.
 * Mapea las propiedades definidas en application.properties con el prefijo "app.jwt".
 * Permite inyectar la clave secreta y el tiempo de expiración.
 */
public class JwtProperties {
	/** Clave secreta para firmar los tokens. */
	public static String secretKey;
	/** Tiempo de validez del token en milisegundos. */
	public static int timeAuthMs;

	public void setSecretKey(String secretKey) {
		JwtProperties.secretKey = secretKey;
	}

	public void setTimeAuthMs(int timeAuthMs) {
		JwtProperties.timeAuthMs = timeAuthMs;
	}
}