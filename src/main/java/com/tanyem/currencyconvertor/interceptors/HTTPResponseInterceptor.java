package com.tanyem.currencyconvertor.interceptors;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@Component
public class HTTPResponseInterceptor implements HandlerInterceptor {

    private final InfluxDBClient influxDBClient;

    public HTTPResponseInterceptor(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        Point point = Point.measurement("rates_request")
                .addField("response_code", response.getStatus())
                .time(Instant.now(), WritePrecision.MS);
        writeApi.writePoint(point);
    }
}
