package com.arsw.client;

import java.net.*;
import java.util.concurrent.*;

public class ConcurrentClient {
    public static void main(String[] args) throws Exception {
        String base = "http://localhost:8080/api/market/alpha/MSFT/daily";
        int solicitudes = 50, hilos = 15;

        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        for (int i = 0; i < solicitudes; i++) {
            pool.submit(() -> {
                try {
                    HttpURLConnection c = (HttpURLConnection) new URL(base).openConnection();
                    c.setRequestMethod("GET");
                    int code = c.getResponseCode();
                    System.out.println("HTTP " + code);
                    c.getInputStream().close();
                } catch (Exception e) { e.printStackTrace(); }
            });
        }
        pool.shutdown();
    }
}