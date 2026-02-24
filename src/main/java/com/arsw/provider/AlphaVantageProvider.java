package com.arsw.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlphaVantageProvider implements MarketDataProvider {

    @Value("${alphavantage.apikey}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE = "https://www.alphavantage.co/query?";

    private String call(String function, String symbol) {
        String url = BASE + "function=" + function +
                "&symbol=" + symbol +
                "&apikey=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }

    @Override public String getIntraDay(String symbol) {
        return call("TIME_SERIES_INTRADAY&interval=5min", symbol);
    }
    @Override public String getDaily(String symbol) {
        return call("TIME_SERIES_DAILY", symbol);
    }
    @Override public String getWeekly(String symbol) {
        return call("TIME_SERIES_WEEKLY", symbol);
    }
    @Override public String getMonthly(String symbol) {
        return call("TIME_SERIES_MONTHLY", symbol);
    }
}
