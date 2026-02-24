package com.arsw.client;

import java.net.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AdvancedConcurrentClient {
    private static final String BASE_URL = "http://localhost:8080/api/market";
    private static final String AUTH_URL = "http://localhost:8080/api/auth";
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Cliente Avanzado de Pruebas de Concurrencia ===");
        
        // 1. Obtener API key
        String apiKey = obtenerApiKey();
        System.out.println("API Key obtenida: " + apiKey.substring(0, 10) + "...");
        
        // 2. Probar endpoints básicos
        probarEndpointsBasicos(apiKey);
        
        // 3. Pruebas de concurrencia
        pruebasConcurrencia(apiKey);
        
        // 4. Pruebas de comparación IA
        pruebasComparacionIA(apiKey);
        
        System.out.println("\n=== Pruebas completadas ===");
    }
    
    private static String obtenerApiKey() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) 
            new URL(AUTH_URL + "/login").openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        String body = "{\"username\":\"admin\"}";
        conn.getOutputStream().write(body.getBytes());
        
        if (conn.getResponseCode() == 200) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String response = br.readLine();
                // Extraer API key del JSON response
                return response.split("\"apiKey\":\"")[1].split("\"")[0];
            }
        }
        throw new RuntimeException("No se pudo obtener API key");
    }
    
    private static void probarEndpointsBasicos(String apiKey) throws Exception {
        System.out.println("\n--- Probando endpoints básicos ---");
        
        String[] symbols = {"MSFT", "AAPL", "GOOGL"};
        String[] types = {"intra", "daily", "weekly", "monthly"};
        String[] providers = {"alpha", "ia"};
        
        for (String symbol : symbols) {
            for (String type : types) {
                for (String provider : providers) {
                    String url = String.format("%s/%s/%s/%s", BASE_URL, provider, symbol, type);
                    int response = hacerRequest(url, apiKey);
                    System.out.printf("Request: %s/%s/%s/%s - Status: %d%n", 
                        provider, symbol, type, response);
                }
            }
        }
    }
    
    private static void pruebasConcurrencia(String apiKey) throws Exception {
        System.out.println("\n--- Pruebas de concurrencia ---");
        
        int solicitudes = 100;
        int hilos = 20;
        
        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        CountDownLatch latch = new CountDownLatch(solicitudes);
        AtomicInteger exitos = new AtomicInteger(0);
        AtomicInteger fallos = new AtomicInteger(0);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < solicitudes; i++) {
            final int requestId = i;
            pool.submit(() -> {
                try {
                    String url = BASE_URL + "/alpha/MSFT/daily";
                    int code = hacerRequest(url, apiKey);
                    if (code == 200) {
                        exitos.incrementAndGet();
                    } else {
                        fallos.incrementAndGet();
                    }
                    System.out.printf("Request %d: HTTP %d%n", requestId, code);
                } catch (Exception e) {
                    fallos.incrementAndGet();
                    System.err.printf("Request %d failed: %s%n", requestId, e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        System.out.printf("\nResultados concurrencia:%n");
        System.out.printf("- Solicitudes totales: %d%n", solicitudes);
        System.out.printf("- Exitos: %d%n", exitos.get());
        System.out.printf("- Fallos: %d%n", fallos.get());
        System.out.printf("- Tiempo total: %d ms%n", endTime - startTime);
        System.out.printf("- Requests/segundo: %.2f%n", 
            solicitudes * 1000.0 / (endTime - startTime));
        
        pool.shutdown();
    }
    
    private static void pruebasComparacionIA(String apiKey) throws Exception {
        System.out.println("\n--- Pruebas de comparación IA ---");
        
        String[][] comparaciones = {
            {"MSFT", "AAPL"},
            {"GOOGL", "MSFT"},
            {"AAPL", "GOOGL"}
        };
        
        for (String[] par : comparaciones) {
            String url = String.format("%s/compare/%s/%s", BASE_URL, par[0], par[1]);
            int response = hacerRequest(url, apiKey);
            System.out.printf("Comparación %s vs %s - Status: %d%n", par[0], par[1], response);
        }
    }
    
    private static int hacerRequest(String url, String apiKey) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-API-Key", apiKey);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(10000);
        
        int responseCode = conn.getResponseCode();
        conn.getInputStream().close();
        return responseCode;
    }
}
