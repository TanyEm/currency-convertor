package com.tanyem.currencyconvertor.configs;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Setter
@Getter
@Configuration
public class InfluxDBConfig {

    private static final Logger logger = LoggerFactory.getLogger(InfluxDBConfig.class);

    @Value("${influxdb.token}")
    private String token;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Value("${influxdb.org}")
    private String org;

    @Value("${influxdb.url}")
    private String url;

    @Bean
    @Primary
    public InfluxDBClient influxDBClient() {
        try {
        return InfluxDBClientFactory.create(getUrl(), getToken().toCharArray(), getOrg(), getBucket());
        } catch (Exception e) {
            logger.error("InfluxDB is not available: {}", e.getMessage());
            return null;
        }
    }
}
