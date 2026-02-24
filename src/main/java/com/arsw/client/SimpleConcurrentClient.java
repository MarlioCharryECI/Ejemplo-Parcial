package com.arsw.client;

import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;

public class SimpleConcurrentClient {
    private static final String BASE_URL = "http://localhost:8080/api/market";
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Cliente Simple de Pruebas de Concurrencia ===");
        
        // 1. Health check
        healthCheck();
        
        // 2. Pruebas básicas
        pruebasBasicas();
        
        // 3. Pruebas de concurrencia multiusuario
        pruebasConcurrenciaMultiusuario();
        
        // 4. Pruebas de comparación IA
        pruebasComparacionIA();
        
        System.out.println("\n=== Pruebas completadas ===");
    }
    
    private static void healthCheck() throws Exception {
        System.out.println("\n--- Health Check ---");
        String url = BASE_URL + "/health";
        int response = hacerRequestSimple(url);
        System.out.printf("Health check: %d%n", response);
    }
    
    private static void pruebasBasicas() throws Exception {
        System.out.println("\n--- Pruebas Básicas ---");
        
        String[] symbols = {"MSFT", "AAPL", "GOOGL"};
        String[] types = {"daily", "weekly"};
        String[] providers = {"alpha", "ia"};
        
        for (String symbol : symbols) {
            for (String type : types) {
                for (String provider : providers) {
                    String url = String.format("%s/%s/%s/%s", BASE_URL, provider, symbol, type);
                    int response = hacerRequestSimple(url);
                    System.out.printf("Request: %s/%s/%s/%s - Status: %d%n", 
                        provider, symbol, type, response);
                }
            }
        }
    }
    
    private static void pruebasConcurrenciaMultiusuario() throws Exception {
        System.out.println("\n--- Pruebas de Concurrencia Multiusuario ---");
        
        int solicitudes = 200;
        int hilos = 25;
        
        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        CountDownLatch latch = new CountDownLatch(solicitudes);
        AtomicInteger exitos = new AtomicInteger(0);
        AtomicInteger fallos = new AtomicInteger(0);
        
        String[] symbols = {"MSFT", "AAPL", "GOOGL", "TSLA", "AMZN"};
        String[] types = {"daily", "weekly", "monthly"};
        String[] providers = {"alpha", "ia"};
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < solicitudes; i++) {
            final int requestId = i;
            pool.submit(() -> {
                try {
                    // Simular diferentes usuarios con requests aleatorios
                    String symbol = symbols[requestId % symbols.length];
                    String type = types[requestId % types.length];
                    String provider = providers[requestId % providers.length];
                    
                    String url = String.format("%s/%s/%s/%s", BASE_URL, provider, symbol, type);
                    int code = hacerRequestSimple(url);
                    
                    if (code == 200) {
                        exitos.incrementAndGet();
                    } else {
                        fallos.incrementAndGet();
                    }
                    
                    if (requestId % 20 == 0) {
                        System.out.printf("Request %d: HTTP %d (%s/%s/%s)%n", 
                            requestId, code, provider, symbol, type);
                    }
                } catch (Exception e) {
                    fallos.incrementAndGet();
                    if (requestId % 20 == 0) {
                        System.err.printf("Request %d failed: %s%n", requestId, e.getMessage());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        System.out.printf("\nResultados concurrencia multiusuario:%n");
        System.out.printf("- Solicitudes totales: %d%n", solicitudes);
        System.out.printf("- Hilos concurrentes: %d%n", hilos);
        System.out.printf("- Exitos: %d%n", exitos.get());
        System.out.printf("- Fallos: %d%n", fallos.get());
        System.out.printf("- Tiempo total: %d ms%n", endTime - startTime);
        System.out.printf("- Requests/segundo: %.2f%n", 
            solicitudes * 1000.0 / (endTime - startTime));
        System.out.printf("- Tasa de éxito: %.1f%%%n", 
            exitos.get() * 100.0 / solicitudes);
        
        pool.shutdown();
    }
    
    private static void pruebasComparacionIA() throws Exception {
        System.out.println("\n--- Pruebas de Comparación IA ---");
        
        String[][] comparaciones = {
            {"MSFT", "AAPL"},
            {"GOOGL", "MSFT"},
            {"AAPL", "GOOGL"},
            {"TSLA", "AMZN"},
            {"MSFT", "TSLA"}
        };
        
        for (String[] par : comparaciones) {
            String url = String.format("%s/compare/%s/%s", BASE_URL, par[0], par[1]);
            int response = hacerRequestSimple(url);
            System.out.printf("Comparación %s vs %s - Status: %d%n", par[0], par[1], response);
        }
    }
    
    private static int hacerRequestSimple(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(8000);
        
        int responseCode = conn.getResponseCode();
        conn.getInputStream().close();
        return responseCode;
    }
}
