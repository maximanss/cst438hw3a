package cst438hw3a.configuration;

/**
 * This will create a Rabbit message exchange - "city-reservation" for the application
 */
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ConfigPublisher {
    
    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("city-reservation");
    }
}
