package com.telemetry.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deviceTelemetryOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Device Telemetry Service API")
                        .description("HomeWhiz-style IoT telemetry platform: device registry, ingest, Redis status, Kafka alerts, dashboard.")
                        .version("1.0.0"));
    }
}
