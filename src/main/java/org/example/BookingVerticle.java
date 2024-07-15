package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.ArrayList;
import java.util.List;

public class BookingVerticle extends AbstractVerticle {

    private final List<JsonObject> bookings = new ArrayList<>();

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        // Disable CSRF protection for testing
        router.route().handler(ctx -> {
            ctx.next();
        });

        router.post("/book").handler(ctx -> {
            System.out.println("Received POST /book request");
            JsonObject booking = ctx.getBodyAsJson();
            System.out.println("Booking data: " + booking.encodePrettily());
            bookings.add(booking);
            JsonObject response = new JsonObject()
                    .put("status", "success")
                    .put("message", "Booking confirmed for " + booking.getString("name") + " on flight " + booking.getString("flightNumber"));
            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(response.encodePrettily());
            System.out.println("Booking added: " + booking.encodePrettily());
        });

        router.get("/bookings").handler(ctx -> {
            System.out.println("Received GET /bookings request");
            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(bookings.toString());
            System.out.println("Bookings retrieved: " + bookings.toString());
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8082, result -> {
                    if (result.succeeded()) {
                        System.out.println("BookingVerticle is deployed and listening on port 8082");
                    } else {
                        System.out.println("Failed to deploy BookingVerticle: " + result.cause());
                    }
                });
    }
}