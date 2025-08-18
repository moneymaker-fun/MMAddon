package de.timuuuu.moneymaker.activities.popup;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.models.OperatingSystem;

@Link("popup/token-verification.lss")
@AutoActivity
public class TokenVerificationActivity extends SimpleActivity {

  private MoneyMakerAddon addon;

  private TokenType tokenType = null;
  private String token = null;

  private boolean error = false;
  private String errorMessage = "";

  public TokenVerificationActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void onCloseScreen() {
    //resetToken();
  }

  @Override
  public boolean keyPressed(Key key, InputType type) {
    if(key == Key.ESCAPE) {
      this.closeScreen();
      this.resetCachedData();
      return true;
    }
    return super.keyPressed(key, type);
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");
    HorizontalListWidget header = new HorizontalListWidget().addId("header");

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.verification.title").addId("title");
    header.addEntry(titleWidget);

    IconWidget closeButton = new IconWidget(SpriteCommon.SMALL_X_WITH_SHADOW).addId("close-button");
    closeButton.setPressable(() -> {
      this.closeScreen();
      this.resetCachedData();
    });
    header.addEntry(closeButton);

    container.addContent(header);

    VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");

    if(this.tokenType != null && this.token != null) {
      content.addChild(ComponentWidget.i18n("moneymaker.verification.activity.tokenGenerated").addId("info-token-text"));
      content.addChild(ComponentWidget.component(Component.translatable("moneymaker.verification.activity.token", Component.text(this.token, NamedTextColor.YELLOW))).addId("info-token"));
      if(this.tokenType == TokenType.WEBSITE) {
        content.addChild(ComponentWidget.i18n("moneymaker.verification.activity.website.infoText").addId("info-website-text"));
        ButtonWidget websiteButton = ButtonWidget.i18n("moneymaker.verification.activity.website.openButton").addId("website-button");
        websiteButton.setPressable(() -> OperatingSystem.getPlatform().openUri("https://moneymakeraddon.de/register?token=" + this.token));
        content.addChild(websiteButton);
      }
      if(tokenType == TokenType.DISCORD) {
        container.addId("discord-container");
        ButtonWidget copyCodeButton = ButtonWidget.i18n("moneymaker.verification.activity.discord.copyToken").addId("copy-token-button");
        copyCodeButton.setPressable(() -> {
          this.addon.labyAPI().minecraft().setClipboard(this.token);
          this.addon.pushNotification(
              Component.translatable("moneymaker.verification.title", NamedTextColor.GREEN),
              Component.translatable("moneymaker.verification.activity.discord.copied", NamedTextColor.GRAY)
          );
        });
        content.addChild(copyCodeButton);
        content.addChild(ComponentWidget.i18n("moneymaker.verification.activity.discord.infoText1").addId("info-discord-text-1"));
        content.addChild(ComponentWidget.component(Component.translatable("moneymaker.verification.activity.discord.infoText2", Component.text("#account-verification", NamedTextColor.YELLOW))).addId("info-discord-text-2"));

        ButtonWidget discordInviteButton = ButtonWidget.i18n("moneymaker.verification.activity.discordButton").addId("discord-invite-button");
        discordInviteButton.setPressable(() -> OperatingSystem.getPlatform().openUri(Util.DISCORD_INVITE_URL));
        content.addChild(discordInviteButton);
      }
    } else {
      if(error) {
        content.addChild(ComponentWidget.i18n("moneymaker.verification.activity.error").addId("info-server-response"));
        content.addChild(ComponentWidget.component(Component.translatable("moneymaker.verification.activity.errorReason", Component.text(this.errorMessage, NamedTextColor.RED))).addId("info-server-wait"));
        ButtonWidget discordInviteButton = ButtonWidget.i18n("moneymaker.verification.activity.discordButton").addId("discord-invite-button");
        discordInviteButton.setPressable(() -> OperatingSystem.getPlatform().openUri(Util.DISCORD_INVITE_URL));
        content.addChild(discordInviteButton);
      } else {
        content.addChild(ComponentWidget.i18n("moneymaker.verification.activity.serverWaiting").addId("info-server-response"));
        content.addChild(ComponentWidget.i18n("moneymaker.verification.activity.serverResponseInfo").addId("info-server-wait"));
        ButtonWidget discordInviteButton = ButtonWidget.i18n("moneymaker.verification.activity.discordButton").addId("discord-invite-button");
        discordInviteButton.setPressable(() -> OperatingSystem.getPlatform().openUri(Util.DISCORD_INVITE_URL));
        content.addChild(discordInviteButton);
      }
    }

    container.addFlexibleContent(content);

    HorizontalListWidget footer = new HorizontalListWidget().addId("footer");
    footer.addEntry(ComponentWidget.i18n("moneymaker.verification.activity.closeInfo").addId("window-close-info"));

    container.addContent(footer);

    this.document.addChild(container);
  }

  public void setToken(TokenType tokenType, String token) {
    this.tokenType = tokenType;
    this.token = token;
    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

  public void setError(String errorMessage) {
    this.error = true;
    this.errorMessage = errorMessage;
    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

  public void resetCachedData() {
    this.tokenType = null;
    this.token = null;
    this.error = false;
    this.errorMessage = "";
  }

  public enum TokenType {
    WEBSITE("website"),
    DISCORD("discord");

    private final String name;

    TokenType(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public static TokenType fromName(String name) {
      for(TokenType type : TokenType.values()) {
        if(type.getName().equals(name)) {
          return type;
        }
      }
      return null;
    }

  }

}
