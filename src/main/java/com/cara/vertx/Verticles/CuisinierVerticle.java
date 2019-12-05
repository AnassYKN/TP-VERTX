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
  }

  @Override
  public void stop() throws Exception {
    System.out.println("Stop of Cuisinier Verticle");
  }
}
