package com.arsw.config;

import com.arsw.provider.AlphaVantageProvider;
import com.arsw.provider.RealIAProvider;
import com.arsw.provider.MarketDataProvider;
import org.springframework.stereotype.Service;

@Service
public class ProviderRouter {

    private final AlphaVantageProvider alpha;
    private final RealIAProvider ia;

    public ProviderRouter(AlphaVantageProvider alpha, RealIAProvider ia) {
        this.alpha = alpha;
        this.ia = ia;
    }

    public MarketDataProvider get(String name) {
        return switch (name.toLowerCase()) {
            case "alpha" -> alpha;
            case "ia"    -> ia;
            default      -> alpha;
        };
    }
}
