package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class FlightStatusVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.get("/status").handler(ctx -> {
            String flightNumber = ctx.request().getParam("flightNumber");
            String status = getStatusForFlight(flightNumber);
            JsonObject response = new JsonObject()
                    .put("flightNumber", flightNumber != null ? flightNumber : "")
                    .put("status", status);
            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(response.encode());
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081, result -> {
                    if (result.succeeded()) {
                        System.out.println("FlightStatusVerticle is deployed and listening on port 8081");
                    } else {
                        System.out.println("Failed to deploy FlightStatusVerticle: " + result.cause());
                    }
                });
    }

    private String getStatusForFlight(String flightNumber) {
        if (flightNumber == null || flightNumber.isEmpty()) {
            return "Unknown";
        }
        // Simplified status logic
        switch (flightNumber) {
            case "EK123":
                return "On Time";
            case "EK124":
                return "Delayed";
            default:
                return "Unknown";
        }
    }
}