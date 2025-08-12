package de.timuuuu.moneymaker.group;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.utils.ApiUtil;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.logging.Logging;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GroupService {

  private final Logging logging = Logging.create("MM-GroupService");

  private static Map<String, Group> groups = new HashMap<>();

  public void loadGroups() {
    groups.clear();
    groups.put("user", DEFAULT_GROUP);
    Request.ofGson(JsonObject.class)
        .url(ApiUtil.BASE_URL + "/groups/")
        .async()
        .connectTimeout(5000)
        .readTimeout(5000)
        .addHeader("User-Agent", "MoneyMaker LabyMod 4 Addon")
        .execute(response -> {
          if(response.hasException()) {
            this.logging.error("Failed to load groups", response.exception());
            return;
          }
          JsonObject object = response.get();
          if(object.has("groups") && object.get("groups").isJsonArray()) {
            object.get("groups").getAsJsonArray().forEach(jsonElement -> {
              if(jsonElement.isJsonObject()) {
                JsonObject groupObject = jsonElement.getAsJsonObject();
                Group group = new Group(
                    groupObject.get("id").getAsInt(),
                    groupObject.get("name").getAsString(),
                    !groupObject.get("displayName").getAsString().isEmpty() ? groupObject.get("displayName").getAsString() : null,
                    groupObject.get("color_hex").getAsString(),
                    groupObject.get("color_minecraft").getAsString(),
                    !groupObject.get("tag_name").getAsString().isEmpty() ? groupObject.get("tag_name").getAsString() : null,
                    groupObject.get("display_type").getAsString(),
                    !groupObject.get("icon_name").getAsString().isEmpty() ? groupObject.get("icon_name").getAsString() : null,
                    groupObject.get("icon_url").getAsString(),
                    groupObject.get("is_admin").getAsBoolean(),
                    groupObject.get("is_staff").getAsBoolean()
                );
                group.initialize();
                groups.put(group.getName(), group);
              }
            });
          }
        });
  }

  public static Group getGroup(String name) {
    return groups.getOrDefault(name, DEFAULT_GROUP);
  }

  public Collection<Group> getGroups() {
    return groups.values();
  }

  public static final Group DEFAULT_GROUP = create(6, "user", null, "AAAAAA");

  private static Group create(int id, String name, String displayName, String colorHex) {
    Group group = new Group(id, name, displayName, colorHex, "§f", "",
        "", "icon_gray", "https://moneymakeraddon.de/addon-assets/lore_gray.png", false, false);
    group.initialize();
    return group;
  }

}
