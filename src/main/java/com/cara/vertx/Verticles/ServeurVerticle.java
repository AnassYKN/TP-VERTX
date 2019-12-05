package com.cara.vertx.Verticles;

import io.vertx.core.AbstractVerticle;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServeurVerticle extends AbstractVerticle {

  final String message="[Serveur]:j'ai une commande";
  final String serveurAddress="restaurant.serveur";
  final String CuisinierAddress="restaurant.cuisinier";
  final String ClientAddress="restaurant.client";

  final int period = 3000;

  final static String messageIntro = "[Serveur] - ";

  //La carte du restaurant
  public static ArrayList<String> menu = (ArrayList<String>) Stream.of("La carbonade flamande", "Welsh", "Le chicon-gratin", "Joues de porc au maroilles", "Flamiche au maroilles", "Plat du Jour", "Tajine", "Couscous Royal").collect(Collectors.toList());


  private int clientId=0;

  @Override
  public void start() throws Exception {
    System.out.println(messageIntro + "Start of Serveur Verticle");

    /**Q2: Instancier l'eventBus
     * cette doc pourrait vous être utile https://www.mednikov.net/vertx-eventbus/
     */

    /*final EventBus eventBus = vertx.eventBus();*/


    /**Q3 Mettre en place la reception des messages envoyés par le Restaurant (typés Client envoyés au format Json)
     *
     */

    /**Q4 & Q5 Comptabiliser les clients du restaurant dans un sharedData
     * https://vertx.io/docs/vertx-core/java/#_asynchronous_counters
     *
     */
  }

  @Override
  public void stop() throws Exception {
    System.out.println("Stop of Serveur Verticle");
  }
}
