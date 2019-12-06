package com.cara.vertx.Verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class CuisinierVerticle extends AbstractVerticle {

  final String serveurAddress="restaurant.serveur";

  final static String messageIntro = "[Cuisinier] - ";

  @Override
  public void start() throws Exception {
    System.out.println(messageIntro + "Start of Cuisinier Verticle");

    /** Q2 Receptionner la commande d'un client
     *
     */
    /*
    //Recevoir un message
    final EventBus eventBus = vertx.eventBus();
    final MessageConsumer<String> consumer = eventBus.consumer(CuisinierAddress);
    consumer.handler(message -> {
      System.out.println("[Cuisinier] <- " + message.body());
    });

     */
  }

  @Override
  public void stop() throws Exception {
    System.out.println("Stop of Cuisinier Verticle");
  }
}
