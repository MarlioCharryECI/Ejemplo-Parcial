package com.arsw.dto;

import java.time.LocalDateTime;

public class MarketDTO {
    
    public static class MarketRequest {
        private String provider;
        private String symbol;
        private String type;
        
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
    
    public static class ComparisonRequest {
        private String symbol1;
        private String symbol2;
        
        public String getSymbol1() { return symbol1; }
        public void setSymbol1(String symbol1) { this.symbol1 = symbol1; }
        public String getSymbol2() { return symbol2; }
        public void setSymbol2(String symbol2) { this.symbol2 = symbol2; }
    }
    
    public static class MarketResponse {
        private boolean success;
        private String data;
        private String provider;
        private String symbol;
        private String type;
        private LocalDateTime timestamp;
        private String cached;
        
        public MarketResponse(boolean success, String data, String provider, 
                            String symbol, String type, boolean cached) {
            this.success = success;
            this.data = data;
            this.provider = provider;
            this.symbol = symbol;
            this.type = type;
            this.timestamp = LocalDateTime.now();
            this.cached = cached ? "true" : "false";
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getData() { return data; }
        public String getProvider() { return provider; }
        public String getSymbol() { return symbol; }
        public String getType() { return type; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getCached() { return cached; }
    }
    
    public static class ComparisonResponse {
        private boolean success;
        private ComparisonData comparison;
        private LocalDateTime timestamp;
        
        public ComparisonResponse(boolean success, ComparisonData comparison) {
            this.success = success;
            this.comparison = comparison;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public ComparisonData getComparison() { return comparison; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    public static class ComparisonData {
        private String symbol1;
        private String symbol2;
        private String data1;
        private String data2;
        private String recommendation;
        private double confidence;
        private String winner;
        private String analysis;
        
        public ComparisonData(String symbol1, String symbol2, String data1, String data2, 
                           String recommendation, double confidence, String winner, String analysis) {
            this.symbol1 = symbol1;
            this.symbol2 = symbol2;
            this.data1 = data1;
            this.data2 = data2;
            this.recommendation = recommendation;
            this.confidence = confidence;
            this.winner = winner;
            this.analysis = analysis;
        }
        
        // Getters
        public String getSymbol1() { return symbol1; }
        public String getSymbol2() { return symbol2; }
        public String getData1() { return data1; }
        public String getData2() { return data2; }
        public String getRecommendation() { return recommendation; }
        public double getConfidence() { return confidence; }
        public String getWinner() { return winner; }
        public String getAnalysis() { return analysis; }
    }
}
