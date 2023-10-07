package de.timuuuu.moneymaker.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ChatHandler implements HttpHandler {

  private String authentication = "MMADDON_CHAT_2023_10";

  private MoneyMakerAddon addon;

  public ChatHandler(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    Map<String, String> parameters = WebServer.queryToMap(httpExchange.getRequestURI().getQuery());
    WebServer.Response response = executeAction(parameters);
    httpExchange.sendResponseHeaders(response.getStatusCode(), response.getContent().length());
    OutputStream os = httpExchange.getResponseBody();
    os.write(response.getContent().getBytes());
    os.close();
  }

  private WebServer.Response executeAction(Map<String, String> parameters) {

    if(parameters.containsKey("auth")) {
      if(parameters.get("auth").equals(authentication)) {
        this.addon.displayMessage("Message Received from API-Server.");
      }
    }

    if(parameters.containsKey("type")) {
      String type = parameters.get("type");

      switch(type) {
        case "setlist":

          if(parameters.containsKey("auth")) {
            String auth = parameters.get("auth");
            if(auth.equals(this.authentication)) {
              return new WebServer.Response(200, "Updated SetList");
            } else {
              return new WebServer.Response(401, "Unauthorized");
            }
          }

          break;
        case "birthdays":

          if(parameters.containsKey("auth")) {
            String auth = parameters.get("auth");
            if(auth.equals(this.authentication)) {
              return new WebServer.Response(200, "Updated Birthdays");
            } else {
              return new WebServer.Response(401, "Unauthorized");
            }
          }

          break;
      }

    }

    return new WebServer.Response(400, "Bad Request");
  }


}
