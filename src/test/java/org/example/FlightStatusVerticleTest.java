package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class FlightStatusVerticleTest {

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new FlightStatusVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @ParameterizedTest
    @CsvSource({
            "EK123,On Time",
            "EK124,Delayed",
            "EK125,Unknown"
    })
    void testFlightStatus(String flightNumber, String expectedStatus, Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        client.get(8081, "localhost", "/status")
                .addQueryParam("flightNumber", flightNumber)
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode());
                    JsonObject body = response.bodyAsJsonObject();
                    assertEquals(flightNumber, body.getString("flightNumber"));
                    assertEquals(expectedStatus, body.getString("status"));
                    testContext.completeNow();
                })));
    }

    @Test
    void testInvalidFlightNumber(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        client.get(8081, "localhost", "/status")
                .addQueryParam("flightNumber", "")
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode());
                    JsonObject body = response.bodyAsJsonObject();
                    assertEquals("", body.getString("flightNumber"));
                    assertEquals("Unknown", body.getString("status"));
                    testContext.completeNow();
                })));
    }

    @Test
    void testMissingFlightNumber(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        client.get(8081, "localhost", "/status")
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode());
                    JsonObject body = response.bodyAsJsonObject();
                    assertEquals("", body.getString("flightNumber"));
                    assertEquals("Unknown", body.getString("status"));
                    testContext.completeNow();
                })));
    }
}