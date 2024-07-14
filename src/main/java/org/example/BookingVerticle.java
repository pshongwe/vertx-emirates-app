package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class BookingVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.post("/book").handler(ctx -> {
            JsonObject booking = ctx.getBodyAsJson();
            // Simplified booking logic
            JsonObject response = new JsonObject()
                    .put("status", "success")
                    .put("message", "Booking confirmed for " + booking.getString("name") + " on flight " + booking.getString("flightNumber"));
            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(response.encodePrettily());
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }
}
