package az.edu.turing.mspayment.model.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-country",url= "${client.ms-country.endpoint}")
public interface CountryClient {

    @GetMapping("/api/countries")
    List< CountryDto> getAvailableCountries(@RequestParam String currency);
}
