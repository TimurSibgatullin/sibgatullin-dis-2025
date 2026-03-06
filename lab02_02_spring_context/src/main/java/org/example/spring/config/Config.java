package org.example.spring.config;

import org.example.spring.component.MarketService;
import org.example.spring.model.Market;
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
