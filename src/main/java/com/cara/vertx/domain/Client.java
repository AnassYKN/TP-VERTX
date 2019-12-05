package com.cara.vertx.domain;

import com.cara.vertx.enums.ClientStatus;
import com.cara.vertx.enums.CommandeStatus;

import java.io.Serializable;

public class Client implements Serializable {

  private int id;
  private int clientStatus;
  private String plat;
  private int commandeStatus;

  public Client() { }

  public Client(int x) {
    this.id = x;
    this.clientStatus= ClientStatus.CLPLACED;
    this.commandeStatus = CommandeStatus.CMDORDERING;
    this.plat="";
  }

  public Client(int id, int clientStatus, String plat, int commandeStatus) {
    this.id = id;
    this.clientStatus = clientStatus;
    this.plat = plat;
    this.commandeStatus = commandeStatus;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getClientStatus() {
    return clientStatus;
  }

  public void setClientStatus(int clientStatus) {
    this.clientStatus = clientStatus;
  }

  public String getPlat() {
    return plat;
  }

  public void setPlat(String plat) {
    this.plat = plat;
  }

  public int getCommandeStatus() {
    return commandeStatus;
  }

  public void setCommandeStatus(int commandeStatus) {
    this.commandeStatus = commandeStatus;
  }

  @Override
  public String toString() {
    return "Client{" +
      "id=" + id +
      ", clientStatus=" + clientStatus +
      ", plat='" + plat + '\'' +
      ", commandeStatus=" + commandeStatus +
      '}';
  }
}
