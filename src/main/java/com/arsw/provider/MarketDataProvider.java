package com.arsw.provider;


public interface MarketDataProvider {
    String getIntraDay(String symbol);
    String getDaily(String symbol);
    String getWeekly(String symbol);
    String getMonthly(String symbol);
}

