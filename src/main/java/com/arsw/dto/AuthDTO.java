package com.arsw.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthDTO {
    
    @Schema(description = "Solicitud de registro de nuevo usuario")
    public static class RegisterRequest {
        @Schema(description = "Nombre de usuario único", example = "trader123", required = true)
        private String username;
        
        @Schema(description = "Correo electrónico del usuario", example = "trader@example.com", required = true)
        private String email;
        
        @Schema(description = "Contraseña del usuario", example = "password123", required = true)
        private String password;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    @Schema(description = "Solicitud de login de usuario")
    public static class LoginRequest {
        @Schema(description = "Nombre de usuario", example = "trader123", required = true)
        private String username;
        
        @Schema(description = "Contraseña del usuario", example = "password123", required = true)
        private String password;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    @Schema(description = "Respuesta de autenticación")
    public static class AuthResponse {
        @Schema(description = "Indica si la operación fue exitosa", example = "true")
        private boolean success;
        
        @Schema(description = "Mensaje de respuesta", example = "Usuario registrado exitosamente")
        private String message;
        
        @Schema(description = "Datos del usuario")
        private UserData user;
        
        public AuthResponse(boolean success, String message, UserData user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public UserData getUser() { return user; }
    }
    
    @Schema(description = "Datos del usuario")
    public static class UserData {
        @Schema(description = "ID único del usuario", example = "1234567890")
        private Long id;
        
        @Schema(description = "Nombre de usuario", example = "trader123")
        private String username;
        
        @Schema(description = "Correo electrónico", example = "trader@example.com")
        private String email;
        
        @Schema(description = "API key para autenticación", example = "550e8400-e29b-41d4-a716-446655440000")
        private String apiKey;
        
        @Schema(description = "Fecha de creación del usuario")
        private java.time.LocalDateTime createdAt;
        
        @Schema(description = "Roles del usuario")
        private java.util.Set<String> roles;
        
        public UserData(Long id, String username, String email, String apiKey, 
                      java.time.LocalDateTime createdAt, java.util.Set<String> roles) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.apiKey = apiKey;
            this.createdAt = createdAt;
            this.roles = roles;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getApiKey() { return apiKey; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
        public java.util.Set<String> getRoles() { return roles; }
    }
}
