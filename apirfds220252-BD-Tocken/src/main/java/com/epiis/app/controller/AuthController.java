package com.epiis.app.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.app.JwtService;
import com.epiis.app.entity.UserEntity;

import com.epiis.app.dataaccess.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // Clase record para recibir las credenciales de login
    record LoginRequest(String userName, String password) {
    }

    // Clase record para devolver el token JWT
    record AuthResponse(String token) {
    }

    /**
     * Endpoint para autenticar usuarios.
     * Recibe nombre de usuario y contraseña.
     * Si las credenciales son válidas, genera y devuelve un token JWT.
     *
     * @param request Objeto con userName y password.
     * @return ResponseEntity con el token o mensaje de error.
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {

            Optional<UserEntity> userOpt = userRepository.findByUsername(request.userName());

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario no encontrado");
            }

            UserEntity user = userOpt.get();

            if (!user.getPassword().equals(request.password())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Contraseña incorrecta");
            }

            // Generar token JWT con claims adicionales (id y username)
            String token = jwtService.generateToken(
                    Map.of(
                            "userName", user.getUsername(),
                            "id", user.getId().toString()));

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }
}
