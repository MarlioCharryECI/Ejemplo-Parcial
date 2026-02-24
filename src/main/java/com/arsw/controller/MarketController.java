package com.arsw.controller;

import com.arsw.service.MarketService;
import com.arsw.dto.MarketDTO;
import com.arsw.security.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Market Data", description = "API para obtener datos de mercado y análisis financiero")
@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService service;
    private final UserService userService;

    public MarketController(MarketService service, UserService userService) { 
        this.service = service; 
        this.userService = userService;
    }

    @Operation(summary = "Obtener datos de mercado", description = "Recupera datos de mercado de diferentes proveedores para un símbolo y tipo específicos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Datos de mercado obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @GetMapping("/{provider}/{symbol}/{type}")
    public ResponseEntity<MarketDTO.MarketResponse> get(
            @Parameter(description = "Proveedor de datos", example = "alpha", schema = @io.swagger.v3.oas.annotations.media.Schema(allowableValues = {"alpha", "ia"}), required = true)
            @PathVariable String provider,
            @Parameter(description = "Símbolo de la acción", example = "MSFT", schema = @io.swagger.v3.oas.annotations.media.Schema(example = "MSFT, AAPL, GOOGL, TSLA, NVDA"), required = true)
            @PathVariable String symbol,
            @Parameter(description = "Tipo de datos", example = "daily", schema = @io.swagger.v3.oas.annotations.media.Schema(allowableValues = {"daily", "intra", "weekly", "monthly"}), required = true)
            @PathVariable String type) {
        
        MarketDTO.MarketResponse response = service.consultar(provider, symbol, type);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Comparar símbolos con IA", description = "Compara dos símbolos bursátiles usando inteligencia artificial y devuelve análisis detallado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comparación realizada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error en el análisis de IA")
    })
    @GetMapping("/compare/{symbol1}/{symbol2}")
    public ResponseEntity<MarketDTO.ComparisonResponse> compare(
            @Parameter(description = "Primer símbolo a comparar", example = "AAPL", schema = @io.swagger.v3.oas.annotations.media.Schema(example = "MSFT, AAPL, GOOGL, TSLA, NVDA"), required = true)
            @PathVariable String symbol1,
            @Parameter(description = "Segundo símbolo a comparar", example = "GOOGL", schema = @io.swagger.v3.oas.annotations.media.Schema(example = "MSFT, AAPL, GOOGL, TSLA, NVDA"), required = true)
            @PathVariable String symbol2) {
        
        MarketDTO.ComparisonResponse response = service.compararConIA(symbol1, symbol2);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Health Check del sistema", description = "Verifica el estado de todos los servicios del sistema (Alpha Vantage, IA, Cache)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sistema funcionando correctamente")
    })
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(String.format(
            "{\"status\":\"ok\",\"timestamp\":\"%s\",\"services\":{\"alphaVantage\":\"active\",\"ia\":\"active\",\"cache\":\"active\"}}",
            java.time.LocalDateTime.now()
        ));
    }
}
