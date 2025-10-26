package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.GsonUtil;
import net.labymod.api.util.StringUtil;
import net.labymod.api.util.logging.Logging;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatMessageLoader {

  private final Logging logging = Logging.create("MM-ChatMessageLoader");

  public HashMap<String, String> availableLanguages = new HashMap<>();

  private final Map<String, String> messages = new HashMap<>();
  private static String selectedLanguage;

  public String message(String key) {
    return this.messages.getOrDefault(key, key);
  }

  public void loadMessages(String namespace, String selectedLanguage) throws IOException {
    this.loadMessages(namespace, selectedLanguage, this.messages);
  }

  private void loadMessages(String namespace, String selectedLanguage, Map<String, String> target) throws IOException {
    selectedLanguage = selectedLanguage.toLowerCase(Locale.ENGLISH);
    if (ChatMessageLoader.selectedLanguage == null || !ChatMessageLoader.selectedLanguage.equals(selectedLanguage)) {
      messages.clear();
      ChatMessageLoader.selectedLanguage = selectedLanguage;
    }

    if (this.existsMessageDirectory(namespace)) {
      String name = String.format(Locale.ROOT, "assets/%s/chat_messages/%s", namespace, StringUtil.toLowercase(selectedLanguage) + ".json");
      ResourceLocation location = ResourceLocation.create(namespace, "chat_messages/" + StringUtil.toLowercase(selectedLanguage) + ".json");
      InputStream inputStream = null;

      try {
        inputStream = location.openStream();
      } catch (IOException ignored) {}

      if (inputStream != null) {
        try {
          InputStreamReader reader;
          label202: {
            label203: {
              reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

              try {
                JsonElement element;
                try {
                  element = GsonUtil.DEFAULT_GSON.fromJson(reader, JsonElement.class);
                } catch (JsonSyntaxException exception) {
                  this.logging.error("Could not load the translations of {}.", namespace, exception);
                  break label202;
                }

                if (!element.isJsonObject()) {
                  this.logging.error("Invalid language file: \"{}:{}\"", namespace, name);
                  break label203;
                }

                JsonObject object = element.getAsJsonObject();

                for(Map.Entry<String, JsonElement> entry : object.entrySet()) {
                  this.readJsonTree(target, entry.getKey(), entry.getValue());
                }
              } catch (Throwable var20) {
                try {
                  reader.close();
                } catch (Throwable var18) {
                  var20.addSuppressed(var18);
                }

                throw var20;
              }

              reader.close();
              return;
            }

            reader.close();
            return;
          }

          reader.close();
        } finally {
          inputStream.close();
        }

      }
    }
  }

  private boolean existsMessageDirectory(String namespace) {
    try {
      InputStream inputStream = getResourceAsInputStream("assets/" + namespace + "/chat_messages/");
      inputStream.close();
      return true;
    } catch (IOException var3) {
      return false;
    }
  }

  private void readJsonTree(Map<String, String> target, String key, JsonElement element) {
    if (!element.isJsonObject()) {
      if (element.isJsonPrimitive()) {
        target.putIfAbsent(key, element.getAsString());
      }
    } else {
      JsonObject object = element.getAsJsonObject();
      for(Map.Entry<String, JsonElement> entry : object.entrySet()) {
        this.readJsonTree(target, key + "." + entry.getKey(), entry.getValue());
      }
    }
  }

  public static InputStream getResourceAsInputStream(String path) throws IOException {
    URL resource = MoneyMakerAddon.class.getClassLoader().getResource(path);
    if (resource == null) {
      throw new FileNotFoundException("No resource was found at this location: (" + path + ")");
    } else {
      return resource.openStream();
    }
  }

}
