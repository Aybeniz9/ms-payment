package az.edu.turing.mspayment.controller;

import az.edu.turing.mspayment.exception.ExceptionResponseFeign;
import az.edu.turing.mspayment.model.client.CountryClient;
import az.edu.turing.mspayment.model.client.CountryDto;
import lombok.extern.slf4j.Slf4j;


import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
public class FallBackController implements CountryClient {
    private final Exception exception;

    public FallBackController(Exception exception) {
        this.exception = exception;
    }

    @Override
    public List<CountryDto> getAvailableCountries(String currency) {
        log.info("This method runned");
        if (exception instanceof TimeoutException) {
            return Collections.emptyList();
        }
        var uuid= UUID.randomUUID().toString();
        throw new ExceptionResponseFeign(uuid,"Service unveliable now... Please retry 30 seconds after");

    }
}
