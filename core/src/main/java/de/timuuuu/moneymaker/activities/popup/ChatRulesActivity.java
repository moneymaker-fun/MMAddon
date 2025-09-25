package de.timuuuu.moneymaker.activities.popup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import java.util.function.Consumer;
import de.timuuuu.moneymaker.utils.ApiUtil;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.util.io.web.request.Request;

@Link("popup/chat-rules.lss")
@AutoActivity
public class ChatRulesActivity extends SimpleActivity {

  public static JsonObject CACHED_RULES;
  private static String CACHED_LANGUAGE;

  private MoneyMakerAddon addon;

  private ScreenInstance previousScreen;
  private boolean updateConfiguration;
  private JsonObject rules;

  public ChatRulesActivity(MoneyMakerAddon addon, ScreenInstance previousScreen, boolean updateConfiguration, JsonObject rules) {
    this.addon = addon;
    this.previousScreen = previousScreen;
    this.updateConfiguration = updateConfiguration;
    this.rules = rules;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");
    HorizontalListWidget header = new HorizontalListWidget().addId("header");

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.chat-rules.title").addId("title");
    header.addEntry(titleWidget);

    if(!this.updateConfiguration) {
      IconWidget closeButton = new IconWidget(SpriteCommon.SMALL_X_WITH_SHADOW).addId("close-button");
      closeButton.setPressable(() -> closeScreen(false));
      header.addEntry(closeButton);
    }

    container.addContent(header);

    VerticalListWidget<ComponentWidget> content = new VerticalListWidget<>().addId("content");

    try {
      if(this.rules.has("header")) {
        content.addChild(ComponentWidget.text(this.rules.get("header").getAsString()).addId("note"));
      }

      if(this.rules.has("rules")) {
        for(JsonElement element : this.rules.get("rules").getAsJsonArray()) {
          if(!(element instanceof JsonObject)) continue;
          JsonObject rule = element.getAsJsonObject();

          if(rule.has("title")) {
            content.addChild(ComponentWidget.text(rule.get("title").getAsString()).addId("title"));
          }

          if(rule.has("text")) {
            content.addChild(ComponentWidget.text(rule.get("text").getAsString()).addId("text"));
          }

        }
      }

      if(this.rules.has("footer")) {
        content.addChild(ComponentWidget.text(this.rules.get("footer").getAsString()).addId("note"));
      }


    } catch (Exception exception) {
      exception.printStackTrace();
      ComponentWidget errorWidget = ComponentWidget.text(exception.getMessage()).addId("note");
      content.addChild(errorWidget);
    }

    container.addFlexibleContent(new ScrollWidget(content).addId("scroll"));

    HorizontalListWidget footer = new HorizontalListWidget().addId("footer");

    if(this.rules != null) {
      if(this.rules.has("version") && this.rules.has("versiondate")) {
        ComponentWidget versionComponent = ComponentWidget.component(
          Component.translatable("moneymaker.chat-rules.version.name").append(Component.text(this.rules.get("version").getAsString()))
              .append(Component.text(" | ").append(Component.translatable("moneymaker.chat-rules.version.date")).append(Component.text(this.rules.get("versiondate").getAsString())))
        );
        versionComponent.addId("version-info");
        footer.addEntry(versionComponent);
      }
    }

    if(this.updateConfiguration) {
      ButtonWidget acceptButton = ButtonWidget.i18n("moneymaker.chat-rules.accept").addId("accept-button");
      acceptButton.setPressable(() -> closeScreen(true));
      footer.addEntry(acceptButton);
    }

    container.addContent(footer);

    this.document.addChild(container);
  }

  private void closeScreen(boolean accepted) {
    if(accepted && this.updateConfiguration && this.rules.has("version")) {
      this.addon.configuration().chatRulesVersion().set(this.rules.get("version").getAsInt());
      this.addon.saveConfiguration();
    }
    this.addon.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
  }

  public static void create(MoneyMakerAddon addon, ScreenInstance previousScreen, boolean updateConfiguration, Consumer<ChatRulesActivity> callback) {
    if(CACHED_RULES != null && (CACHED_LANGUAGE != null && CACHED_LANGUAGE.equals(addon.labyAPI().minecraft().options().getCurrentLanguage()))) {
      addon.labyAPI().minecraft().executeNextTick(() -> callback.accept(new ChatRulesActivity(addon, previousScreen, updateConfiguration, CACHED_RULES)));
      return;
    }
    Request.ofGson(JsonObject.class)
        .url(ApiUtil.BASE_URL + "/chat/rules/?lang=" + addon.labyAPI().minecraft().options().getCurrentLanguage())
        .async()
        .connectTimeout(5000)
        .readTimeout(5000)
        .execute(response -> {
          if(response.getStatusCode() != 200 || response.hasException()) {
            callback.accept(null);
            return;
          }
          addon.labyAPI().minecraft().executeNextTick(() -> callback.accept(new ChatRulesActivity(addon, previousScreen, updateConfiguration, response.get())));
          CACHED_RULES = response.get();
          CACHED_LANGUAGE = addon.labyAPI().minecraft().options().getCurrentLanguage();
        });
  }

  public JsonObject rules() {
    return rules;
  }
}
