package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.deployVerticle(new BookingVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("BookingVerticle deployed successfully");
            } else {
                System.out.println("BookingVerticle deployment failed: " + res.cause());
            }
        });
        vertx.deployVerticle(new FlightStatusVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("FlightStatusVerticle deployed successfully");
            } else {
                System.out.println("FlightStatusVerticle deployment failed: " + res.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
