package com.arsw.service;

import com.arsw.config.ProviderRouter;
import com.arsw.cache.SimpleCache;
import com.arsw.provider.MarketDataProvider;
import org.springframework.stereotype.Service;

@Service
public class EnhancedMarketService {

    private final ProviderRouter router;
    private final SimpleCache cache;

    public EnhancedMarketService(ProviderRouter router, SimpleCache cache) {
        this.router = router; this.cache = cache;
    }

    public String consultar(String provider, String symbol, String type) {
        String key = "%s-%s-%s".formatted(provider, symbol, type);
        return cache.computeIfAbsent(key, () -> {
            MarketDataProvider p = router.get(provider);
            return switch (type.toLowerCase()) {
                case "intra"   -> p.getIntraDay(symbol);
                case "daily"   -> p.getDaily(symbol);
                case "weekly"  -> p.getWeekly(symbol);
                case "monthly" -> p.getMonthly(symbol);
                default        -> p.getDaily(symbol);
            };
        });
    }
    
    public String compararConIA(String symbol1, String symbol2) {
        String key = "ia-compare-%s-%s".formatted(symbol1, symbol2);
        return cache.computeIfAbsent(key, () -> {
            MarketDataProvider iaProvider = router.get("ia");
            String data1 = iaProvider.getDaily(symbol1);
            String data2 = iaProvider.getDaily(symbol2);
            
            return """
            {
                "comparison": {
                    "symbol1": "%s",
                    "symbol2": "%s",
                    "data1": %s,
                    "data2": %s,
                    "recommendation": "basado en análisis comparativo",
                    "confidence": 0.75
                }
            }
            """.formatted(symbol1, symbol2, data1, data2);
        });
    }
}
