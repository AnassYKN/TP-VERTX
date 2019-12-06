package com.cara.vertx.Verticles;

import com.cara.vertx.domain.Client;
import com.cara.vertx.enums.ClientStatus;
import com.cara.vertx.enums.CommandeStatus;
import com.cara.vertx.utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.Counter;
import io.vertx.core.shareddata.SharedData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RestaurantVerticle extends AbstractVerticle {

  /**Le Verticle Restaurant simule l'activité de clients en envoyant ponctuellement des Objets Client a destination des serveurs qui doivent se charger de les accueillir et de les servir
   * Il dispose aussi de la carte du restaurant pour permettre au client de passer sa commande
   */

  //La carte du restaurant
  public static ArrayList<String> menu = (ArrayList<String>) Stream.of(
    "La carbonade flamande",
    "Welsh",
    "Le chicon-gratin",
    "Joues de porc au maroilles",
    "Flamiche au maroilles",
    "Plat du Jour",
    "Tajine",
    "Couscous Royal")
    .collect(Collectors.toList());

  final String serveurAddress ="restaurant.serveur";
  final String clientAddress ="restaurant.client";
  final int period = 3000;
  private int clientId=0;
  final static String clientMessageIntro = "[Client] -";

  @Override
  public void start() throws Exception {
    System.out.println("Start of Restaurant Verticle");

    final EventBus eventbus = vertx.eventBus();

    vertx.setPeriodic(period, (l) -> {
      //On crée un Objet Client pour simuler l'entrée d'un client
      Client client = new Client(++clientId);
      //On adresse un message vers les Verticles serveurAddress au format JSON
      JsonObject jsonToEncode = ClientObjectToJson(client);
      //Definir le head dans le message envoyé
      DeliveryOptions options = utils.getDeliveryOptions("Restaurant", "Serveur");
      eventbus.request(serveurAddress, jsonToEncode, options,  ar->{
        if (ar.succeeded()){
          System.out.println("[Restaurant] Client pris en charge");
        }
        else {
          System.out.println("[Restaurant]: Echec de l'envoi à : "+serveurAddress);
        }
      });
    });

    //recevoir la reponse de Serveur
    eventbus.consumer(clientAddress,res->{
      System.out.println("[Client] <- "+ res.body());
    });
  }


  /** Function qui retourne un élément aléatoire d'une liste
   * @param list
   * @return String
   */
  public static String getRandomElement(List<String> list)
  {
    Random rand = new Random();
    return list.get(rand.nextInt(list.size()));
  }

  /**ClientObjectToJson convertit un Objet Client en Json pour qu'il puisse être envoyé sur l'eventBus
   * @param client
   * @return JsonObject
   */
  public static JsonObject ClientObjectToJson(Client client) {
    JsonObject jsonToEncode = new JsonObject();
    jsonToEncode.put("id", client.getId());
    jsonToEncode.put("clientStatus", client.getClientStatus());
    jsonToEncode.put("plat", client.getPlat());
    jsonToEncode.put("commandeStatus", client.getCommandeStatus());
    return jsonToEncode;
  }

  @Override
  public void stop() throws Exception {
    System.out.println("Stop of Restaurant Verticle");
  }
}
