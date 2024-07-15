package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class BookingVerticleTest {

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new BookingVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void testBookingCreation(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject booking = new JsonObject()
                .put("name", "John Doe")
                .put("flightNumber", "ABC123");

        client.post(8080, "localhost", "/book")
                .sendJsonObject(booking, testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode());
                    JsonObject body = response.bodyAsJsonObject();
                    assertEquals("success", body.getString("status"));
                    assertTrue(body.getString("message").contains("John Doe"));
                    assertTrue(body.getString("message").contains("ABC123"));
                    testContext.completeNow();
                })));
    }

    @Test
    void testGetBookings(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        // First, create a booking
        JsonObject booking = new JsonObject()
                .put("name", "Jane Smith")
                .put("flightNumber", "XYZ789");

        client.post(8080, "localhost", "/book")
                .sendJsonObject(booking, testContext.succeeding(postResponse -> {
                    // Then, get all bookings
                    client.get(8080, "localhost", "/bookings")
                            .send(testContext.succeeding(getResponse -> testContext.verify(() -> {
                                assertEquals(200, getResponse.statusCode());
                                String body = getResponse.bodyAsString();
                                assertTrue(body.contains("Jane Smith"));
                                assertTrue(body.contains("XYZ789"));
                                testContext.completeNow();
                            })));
                }));
    }
}