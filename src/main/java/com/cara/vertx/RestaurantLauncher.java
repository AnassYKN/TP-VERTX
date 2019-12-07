package com.cara.vertx;

import com.cara.vertx.Verticles.*;
import io.vertx.core.*;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class RestaurantLauncher {

  public static int restaurantSize;
  public static int clientsNb;
  public static int cuisiniersNb;

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Start of RestaurantLauncher");

    try (InputStream input = RestaurantLauncher.class.getClassLoader().getResourceAsStream("application.properties")) {

      Properties prop = new Properties();

      // load a properties file
      prop.load(input);

      // get the property value and print it out
      restaurantSize= Integer.parseInt(prop.getProperty("restaurant.nbPlaces"));
      clientsNb 	= Integer.parseInt(prop.getProperty("restaurant.clients.nb"));
      cuisiniersNb = Integer.parseInt(prop.getProperty("restaurant.cuisiniers.nb"));

    } catch (IOException ex) {
      ex.printStackTrace();
    }

    /**Question 1
     * Faire en sorte qu'il y ait plus d'une instance de Serveur et de Cuisinier.
     * Configurer le deploiement pour démarrer les serveurs au nombre de serveursNb(application.properties)
     * Configurer le deploiement pour démarrer les cuisiniers au nombre de cuisiniersNb(application.properties)
     */
    //Configuration d'options de déploiement
    final DeploymentOptions cuisinierOptions = new DeploymentOptions().setInstances(cuisiniersNb);
    IgniteConfiguration cfg = new IgniteConfiguration();
    ClusterManager clusterManager = new IgniteClusterManager(cfg);

    final VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
    Vertx.clusteredVertx(options, res -> {
        if (res.succeeded()) {
            System.out.println("Restaurant Verticle Deployed");
          final Vertx vertx = res.result();
            //Cuisinier
            vertx.deployVerticle(CuisinierVerticle.class.getName(),cuisinierOptions);
            //restaurant
            vertx.deployVerticle(RestaurantVerticle.class.getName());
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

        }
        else {
          System.out.println("Fail to deploy Restaurant");
        }
      });
    System.out.println("End of RestaurantLauncher");
  }
}
