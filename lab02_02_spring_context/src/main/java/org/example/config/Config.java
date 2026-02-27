package org.example.config;

import org.example.component.MarketService;
import org.example.model.Market;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("org.example.component")
public class Config {
    @Bean
    public MarketService getService() {
        Market market = new Market();
        return new MarketService(market);
    }
}
