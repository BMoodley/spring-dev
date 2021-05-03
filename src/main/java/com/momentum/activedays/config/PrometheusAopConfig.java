package com.momentum.activedays.config;

import org.springframework.context.annotation.Configuration;
import io.micrometer.core.aop.TimedAspect;
import org.springframework.context.annotation.Bean;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class PrometheusAopConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
