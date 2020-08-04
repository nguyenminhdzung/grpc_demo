package com.dmn.assignment.task1.configuration;

import com.dmn.assignment.task1.endpoint.CinemaEndpoint;
import com.dmn.assignment.task1.service.CinemaService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesUnitTestConfiguration {
    @Bean
    public CinemaService cinemaService() {
        return Mockito.mock(CinemaService.class);
    }

    @Bean
    public CinemaEndpoint cinemaEndpoint() {
        return new CinemaEndpoint();
    }
}
