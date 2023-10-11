package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.activity.Document;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.models.OperatingSystem;

public class Util {

  public static void addFeedbackButton(Document document) {
    ButtonWidget feedbackButton = ButtonWidget.text("§6Feedback §7/ §cBugreport");
    feedbackButton.setPressable(() -> {
      OperatingSystem.getPlatform().openUrl("https://forms.gle/rWteNnvwqC5Q9Pz76");
    });
    feedbackButton.addId("feedback-button");
    document.addChild(feedbackButton);
  }

  public static void drawAuthor(LabyAPI labyAPI, Bounds bounds, Stack stack) {
    TextRenderer textRenderer = labyAPI.renderPipeline().textRenderer();

    textRenderer.text("§7Addon-Version§8: §e" + MoneyMakerAddon.instance().addonInfo().getVersion())
        .scale(0.8f)
        .pos(5, bounds.getHeight() -17)
        .render(stack);
    textRenderer.text("§7Developed by §eTimuuuu §7& §eMisterCore")
        .scale(0.8f)
        .pos(5, bounds.getHeight() -7)
        .render(stack);
  }

}
