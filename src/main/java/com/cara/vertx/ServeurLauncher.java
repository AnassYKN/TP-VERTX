package com.cara.vertx;

import com.cara.vertx.Verticles.ServeurVerticle;
import io.vertx.core.*;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.configuration.IgniteConfiguration;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServeurLauncher {

  public static int serveursNb;

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Start of VerticleLauncher");

    try (InputStream input = RestaurantLauncher.class.getClassLoader().getResourceAsStream("application.properties")) {

      Properties prop = new Properties();
      // load a properties file
      prop.load(input);

      serveursNb = Integer.parseInt(prop.getProperty("restaurant.serveurs.nb"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    //Configuration d'options de déploiement
    final DeploymentOptions serveurOptions = new DeploymentOptions().setInstances(serveursNb);
    IgniteConfiguration cfg = new IgniteConfiguration();
    ClusterManager clusterManager = new IgniteClusterManager(cfg);
    final VertxOptions options = new VertxOptions().setClusterManager(clusterManager);

    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        System.out.println("Serveur Verticle Deployed");
        final Vertx vertx = res.result();
        vertx.deployVerticle(ServeurVerticle.class.getName(), serveurOptions);
      }else{
        System.out.println("Fail to deploy Serveur");
      }
    });

    System.out.println("End of ServeurLauncher");


  }

}


