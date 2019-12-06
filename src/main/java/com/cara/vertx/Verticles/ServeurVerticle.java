package com.cara.vertx.Verticles;

import com.cara.vertx.domain.Client;
import com.cara.vertx.enums.ClientStatus;
import com.cara.vertx.enums.CommandeStatus;
import com.cara.vertx.utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Counter;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cara.vertx.Verticles.RestaurantVerticle.ClientObjectToJson;
import static com.cara.vertx.Verticles.RestaurantVerticle.getRandomElement;

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

    final EventBus eventBus = vertx.eventBus();


    /**Q3 Mettre en place la reception des messages envoyés par le Restaurant (typés Client envoyés au format Json)
     *
     */

    eventBus.consumer(serveurAddress, res -> {

      //receive a message
      JsonObject jsonObject = JsonObject.mapFrom(res.body());
      Client client = jsonObject.mapTo(Client.class);
      System.out.println("[Serveur] consumer1 <- " + res.body());

      //modifier status client to waiting
      client.setClientStatus(ClientStatus.CLWAITING);
      client.setCommandeStatus(CommandeStatus.CMDORDERED);
      client.setPlat(getRandomElement(menu));

      JsonObject jsonToEncode = ClientObjectToJson(client);

      //Vers -> Cuisinier
      //Definir le head dans le message envoyé
      DeliveryOptions options = utils.getDeliveryOptions("Serveur", "Cuisinier");

      eventBus.send(CuisinierAddress,jsonToEncode, options);

      long add = 1;
      vertx
        .sharedData()
        .getCounter(
          "nbPlacesRestaurant",
          resultHandler -> {
            final Counter counter = resultHandler.result();
            counter.addAndGet(
              add,
              hhh -> {
                System.out.println("Nombre de Clients dans le Restaurant:" + hhh.result());
              });
          });
      //System.out.println(Json.encode(menu));
    });

    //Recevoir la reponse de cuisinier
    eventBus.consumer(serveurAddress,req->{
      if (!req.headers().isEmpty()) {
        utils.logFromTo(req);

        if (req.headers().get("Sender").equals("Cuisinier")) {
          System.out.println("[Serveur] consumer2 <-" + req.body());

          //receive a message
          JsonObject jsonObject = JsonObject.mapFrom(req.body());
          Client client = jsonObject.mapTo(Client.class);

          //modifier status client to waiting
          client.setClientStatus(ClientStatus.CLORDERPASSED);
          client.setCommandeStatus(CommandeStatus.CMDSERVED);

          JsonObject jsonToEncode = ClientObjectToJson(client);
          //Vers -> Client
          //Definir le head dans le message envoyé
          DeliveryOptions options = utils.getDeliveryOptions("Serveur", "Client");
          eventBus.send(ClientAddress,jsonToEncode, options);

        }
      }
    });

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
