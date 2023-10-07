package de.timuuuu.moneymaker.webserver;

import com.sun.net.httpserver.HttpServer;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class WebServer {

  private static HttpServer httpServer = null;

  public static void startService(MoneyMakerAddon addon) {
    if(httpServer != null) {
      return;
    }

    try {
      httpServer = HttpServer.create(new InetSocketAddress(8190), 0);
      httpServer.createContext("/chat", new ChatHandler(addon));

      httpServer.setExecutor(null);
      httpServer.start();
      addon.logger().info("Started WebServer on Port 8190");
    } catch(IOException e) {
      e.printStackTrace();
    }

  }

  public static void stopService() {
    if(httpServer != null) {
      httpServer.stop(2);
    }
  }

  public static Map<String, String> queryToMap(String query){
    Map<String, String> result = new HashMap<>();
    for (String param : query.split("&")) {
      String[] pair = param.split("=");
      if (pair.length>1) {
        result.put(pair[0], pair[1]);
      }else{
        result.put(pair[0], "");
      }
    }
    return result;
  }

  public static class Response {

    private int statusCode;
    private String content;

    public Response(int statusCode, String content) {
      this.statusCode = statusCode;
      this.content = content;
    }

    public int getStatusCode() {
      return statusCode;
    }

    public String getContent() {
      return content;
    }
  }

}
