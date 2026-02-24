package com.arsw.provider;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RealIAProvider implements MarketDataProvider {
    
    private final Random random = new Random();
    
    @Override
    public String getIntraDay(String symbol) {
        return generateRealAnalysis(symbol, "intraday");
    }
    
    @Override
    public String getDaily(String symbol) {
        return generateRealAnalysis(symbol, "daily");
    }
    
    @Override
    public String getWeekly(String symbol) {
        return generateRealAnalysis(symbol, "weekly");
    }
    
    @Override
    public String getMonthly(String symbol) {
        return generateRealAnalysis(symbol, "monthly");
    }
    
    private String generateRealAnalysis(String symbol, String timeframe) {
        // Simular análisis real basado en factores técnicos
        double score = calculateTechnicalScore(symbol);
        String recommendation = getRecommendation(score);
        String[] factors = getTechnicalFactors(symbol);
        
        return """
        {
            "symbol": "%s",
            "timeframe": "%s",
            "analysis": {
                "score": %.2f,
                "recommendation": "%s",
                "confidence": %.2f,
                "factors": %s,
                "technicalIndicators": {
                    "rsi": %.2f,
                    "macd": %.2f,
                    "volume": %.2f,
                    "volatility": %.2f
                },
                "riskLevel": "%s",
                "targetPrice": %.2f,
                "stopLoss": %.2f,
                "analysisDate": "%s"
            }
        }
        """.formatted(
            symbol, timeframe, score, recommendation, 
            score * 0.1 + 0.7, // confidence basado en score
            Arrays.toString(factors),
            30 + random.nextDouble() * 40, // RSI
            random.nextDouble() * 2 - 1, // MACD
            random.nextDouble() * 1000000, // Volume
            random.nextDouble() * 0.5, // Volatility
            getRiskLevel(score),
            getCurrentPrice(symbol) * (1 + score * 0.1), // Target
            getCurrentPrice(symbol) * (1 - score * 0.05), // Stop loss
            java.time.LocalDateTime.now().toString()
        );
    }
    
    private double calculateTechnicalScore(String symbol) {
        // Simular cálculo técnico real basado en el símbolo
        int hash = symbol.hashCode();
        double baseScore = (hash % 100) / 100.0;
        
        // Agregar factores técnicos simulados
        double momentum = Math.sin(hash * 0.1) * 0.3;
        double volume = Math.cos(hash * 0.05) * 0.2;
        double volatility = Math.sin(hash * 0.2) * 0.1;
        
        return Math.max(-1, Math.min(1, baseScore + momentum + volume + volatility));
    }
    
    private String getRecommendation(double score) {
        if (score > 0.3) return "FUERTE_COMPRA";
        if (score > 0.1) return "COMPRA";
        if (score > -0.1) return "MANTENER";
        if (score > -0.3) return "VENTA";
        return "FUERTE_VENTA";
    }
    
    private String[] getTechnicalFactors(String symbol) {
        List<String> factors = new ArrayList<>();
        int hash = Math.abs(symbol.hashCode());
        
        if (hash % 3 == 0) factors.add("tendencia_alcista");
        if (hash % 5 == 0) factors.add("volumen_alto");
        if (hash % 7 == 0) factors.add("rsi_sobrecompra");
        if (hash % 11 == 0) factors.add("soporte_resistencia");
        if (hash % 13 == 0) factors.add("volatilidad_media");
        
        if (factors.isEmpty()) {
            factors.add("analisis_neutro");
        }
        
        return factors.toArray(new String[0]);
    }
    
    private String getRiskLevel(double score) {
        double absScore = Math.abs(score);
        if (absScore > 0.7) return "ALTO";
        if (absScore > 0.4) return "MEDIO";
        return "BAJO";
    }
    
    private double getCurrentPrice(String symbol) {
        // Simular precio actual basado en símbolo
        int hash = Math.abs(symbol.hashCode());
        return 50 + (hash % 500) + random.nextDouble() * 10;
    }
}
