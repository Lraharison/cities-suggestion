package com.cyranno.citiessuggestion;

import com.cyranno.citiessuggestion.generated.ApiGatewayResponse;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerTest {

    private Handler handler;

    @Before
    public void setUp() {
        this.handler = new Handler();
    }

    @Test
    public void shouldHandleRequest() {
        final Map<String, Object> input = ImmutableMap.of("queryStringParameters",
                ImmutableMap.of(
                        "q", "antanana",
                        "latitude", "-18.91300",
                        "longitude", "47.53580"
                ));

        final ApiGatewayResponse apiGatewayResponse = this.handler.handleRequest(input, null);

        assertThat(apiGatewayResponse.getStatusCode()).isEqualTo(200);
        assertThat(apiGatewayResponse.getBody()).contains("Antananarivo", "Ambanitsena");
    }

    @Test
    public void shouldReturnError400IfParameterIsInvalid() {
        final Map<String, Object> input = ImmutableMap.of("queryStringParameters",
                ImmutableMap.of(
                        "q", "antanana",
                        "latitude", "aaaa",
                        "longitude", "47.53580"
                ));

        final ApiGatewayResponse apiGatewayResponse = this.handler.handleRequest(input, null);

        assertThat(apiGatewayResponse.getStatusCode()).isEqualTo(400);
        assertThat(apiGatewayResponse.getBody()).contains("invalid");
    }
}
