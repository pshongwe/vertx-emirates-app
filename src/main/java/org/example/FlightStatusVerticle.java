package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class FlightStatusVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.get("/status").handler(ctx -> {
            String flightNumber = ctx.request().getParam("flightNumber");
            // Simplified status logic
            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end("{\"flightNumber\": \"" + flightNumber + "\", \"status\": \"On Time\"}");
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081);
    }
}
