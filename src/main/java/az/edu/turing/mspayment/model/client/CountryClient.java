package az.edu.turing.mspayment.model.client;

import az.edu.turing.mspayment.controller.FallBackController;
import feign.Feign;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.feign.FeignDecorator;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-country", url = "${client.ms-country.endpoint}",
fallback = FallBackController.class,configuration = CountryClient.FeignConfiguration.class)
public interface CountryClient {

    @GetMapping("/api/countries")
    List<CountryDto> getAvailableCountries(@RequestParam String currency);


    class FeignConfiguration {
        private final CircuitBreakerRegistry registry;

        public FeignConfiguration(CircuitBreakerRegistry registry) {
            this.registry = registry;
        }

        @Bean
        public Feign.Builder feignBuilder() {
            CircuitBreaker circuitBreaker = registry.circuitBreaker("countryService");
            FeignDecorators decorators = FeignDecorators.builder()
                    .withCircuitBreaker(circuitBreaker).
                    withFallbackFactory(FallBackController::new).build();
            return Resilience4jFeign.builder(decorators);
        }

    }
}
