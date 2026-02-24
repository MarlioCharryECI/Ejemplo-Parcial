package com.arsw.controller;

import com.arsw.security.UserService;
import com.arsw.security.User;
import com.arsw.dto.AuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Tag(name = "Authentication", description = "API para autenticación y gestión de usuarios")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario y genera una API key para acceder a los endpoints del mercado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El nombre de usuario ya existe")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthDTO.AuthResponse> register(
            @Parameter(description = "Datos de registro del nuevo usuario", required = true)
            @RequestBody AuthDTO.RegisterRequest request) {
        try {
            User newUser = userService.createUser(request.getUsername(), request.getEmail());
            
            AuthDTO.UserData userData = new AuthDTO.UserData(
                newUser.getId(), newUser.getUsername(), newUser.getEmail(), 
                newUser.getApiKey(), java.time.LocalDateTime.now(), java.util.Set.of("USER")
            );
            
            return ResponseEntity.ok(new AuthDTO.AuthResponse(true, "Usuario registrado exitosamente", userData));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new AuthDTO.AuthResponse(false, e.getMessage(), null));
        }
    }
    
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve su API key para usar en las peticiones al mercado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso, devuelve API key"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthDTO.AuthResponse> login(
            @Parameter(description = "Credenciales de login", required = true)
            @RequestBody AuthDTO.LoginRequest request) {
        Optional<User> userOpt = userService.findByUsername(request.getUsername());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Simular validación de password (en producción usar hashing)
            AuthDTO.UserData userData = new AuthDTO.UserData(
                user.getId(), user.getUsername(), user.getEmail(), 
                user.getApiKey(), java.time.LocalDateTime.now(), java.util.Set.of("USER")
            );
            
            return ResponseEntity.ok(new AuthDTO.AuthResponse(true, "Login exitoso", userData));
        }
        
        return ResponseEntity.status(401)
            .body(new AuthDTO.AuthResponse(false, "Credenciales inválidas", null));
    }
    
    @Operation(summary = "Validar API Key", description = "Verifica si una API key es válida y devuelve información del usuario asociado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API key válida, devuelve info del usuario"),
        @ApiResponse(responseCode = "401", description = "API key inválida")
    })
    @GetMapping("/validate/{apiKey}")
    public ResponseEntity<?> validateApiKey(
            @Parameter(description = "API key a validar (formato UUID)", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
            @PathVariable String apiKey) {
        if (userService.validateApiKey(apiKey)) {
            Optional<User> user = userService.findByApiKey(apiKey);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "user", Map.of(
                        "id", user.get().getId(),
                        "username", user.get().getUsername()
                    )
                ));
            }
        }
        
        return ResponseEntity.status(401)
            .body(Map.of("valid", false, "message", "API key inválida"));
    }
}
