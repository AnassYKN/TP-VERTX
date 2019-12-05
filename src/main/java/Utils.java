import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class Utils {



  public static DeliveryOptions getDeliveryOptions(String from, String to) {
    DeliveryOptions options = new DeliveryOptions();
    options.addHeader("Sender", from);
    options.addHeader("Receiver", to);
    return options;
  }

  public static void logFromTo(Message<Object> req) {
    String info= "[" + req.headers().get("Sender")+"] to ["+req.headers().get("Receiver") + "]";
    System.out.print(info);
  }

  public static void logFromToJO(Message<JsonObject> message) {
    String info= "[" + message.headers().get("Sender")+"] to ["+message.headers().get("Receiver") + "]";
    System.out.print(info);
  }

}
