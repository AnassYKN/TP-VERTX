package com.cara.vertx.Verticles;

import com.cara.vertx.domain.Client;
import com.cara.vertx.enums.ClientStatus;
import com.cara.vertx.enums.CommandeStatus;
import com.cara.vertx.utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import static com.cara.vertx.Verticles.RestaurantVerticle.ClientObjectToJson;
import static com.cara.vertx.Verticles.RestaurantVerticle.getRandomElement;

public class CuisinierVerticle extends AbstractVerticle {

  final String serveurAddress="restaurant.serveur";
  final String CuisinierAddress="restaurant.cuisinier";

  final static String messageIntro = "[Cuisinier] - ";

  @Override
  public void start() throws Exception {
    System.out.println(messageIntro + "Start of Cuisinier Verticle");

    /** Q2 Receptionner la commande d'un client
     *
     */
    //Recevoir un message
    final EventBus eventBus = vertx.eventBus();
    final MessageConsumer<JsonObject> consumer = eventBus.consumer(CuisinierAddress);
    consumer.handler(message -> {
      utils.logFromToJO(message);
      System.out.println(": [Cuisinier] Reception d'une commande <- " + message.body());
      JsonObject jsonObject = JsonObject.mapFrom(message.body());
      Client client = jsonObject.mapTo(Client.class);
      //modifier status client to waiting
      //client.setClientStatus(ClientStatus.CLWAITING);
      client.setCommandeStatus(CommandeStatus.CMDCOOKED);
      JsonObject jsonToEncode = ClientObjectToJson(client);
      //Definir le head dans le message envoyé
      DeliveryOptions options = new DeliveryOptions();
      options.addHeader("Sender", "Cuisinier");
      options.addHeader("Receiver", "Serveur");
      //Vers -> Serveur
      eventBus.send(serveurAddress,jsonToEncode,options);
    });








  }

  @Override
  public void stop() throws Exception {
    System.out.println("Stop of Cuisinier Verticle");
  }
}
