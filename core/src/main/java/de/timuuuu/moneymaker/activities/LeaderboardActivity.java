package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.LeaderboardEntryWidget;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.render.matrix.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@AutoActivity
@Links({@Link("leaderboard.lss"), @Link("buttons.lss")})
public class LeaderboardActivity extends SimpleActivity {

  private MoneyMakerAddon addon;

  private SortType sorting = SortType.RANKING;
  private SortDirection sortDirection = SortDirection.DESC;

  private List<LeaderboardEntryWidget> entries = new ArrayList<>();

  private final int updateTime = 10*60*1000;
  private long updateCoolDown = 0;

  public LeaderboardActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.leaderboard.title").addId("title");
    this.document.addChild(titleWidget);

    DivWidget container = new DivWidget();
    container.addId("container");

    ButtonWidget toggleBlocksButton = ButtonWidget.component(
        Component.translatable("moneymaker.ui.leaderboard.toggleBlocks." + (this.addon.addonUtil().leaderboardShowBlocks() ? "enabled": "disabled"), NamedTextColor.YELLOW)
    ).addId("toggle-blocks-button");
    toggleBlocksButton.setPressable(() -> {
      if(this.addon.addonUtil().leaderboardShowBlocks()) {
        this.addon.addonUtil().leaderboardShowBlocks(false);
        this.addon.pushNotification(
            Component.translatable("moneymaker.notification.leaderboard.title", NamedTextColor.GOLD),
            Component.translatable("moneymaker.notification.leaderboard.blocks.disabled", NamedTextColor.RED)
                .append(Component.text("\n"))
                .append(Component.translatable("moneymaker.notification.leaderboard.blocks.info", NamedTextColor.GRAY))
        );
      } else {
        this.addon.addonUtil().leaderboardShowBlocks(true);
        this.addon.pushNotification(
            Component.translatable("moneymaker.notification.leaderboard.title", NamedTextColor.GOLD),
            Component.translatable("moneymaker.notification.leaderboard.blocks.enabled", NamedTextColor.GREEN)
                .append(Component.text("\n"))
                .append(Component.translatable("moneymaker.notification.leaderboard.blocks.info", NamedTextColor.GRAY))
        );
      }
      toggleBlocksButton.updateComponent(
          Component.translatable("moneymaker.ui.leaderboard.toggleBlocks." + (this.addon.addonUtil().leaderboardShowBlocks() ? "enabled": "disabled"), NamedTextColor.YELLOW)
      );
    });
    container.addChild(toggleBlocksButton);

    ButtonWidget updateButton = ButtonWidget.component(
        Component.translatable("moneymaker.ui.leaderboard.update", NamedTextColor.GREEN)
    ).addId("update-button");
    updateButton.setPressable(() -> {
      long remaining = (this.updateCoolDown + this.updateTime - System.currentTimeMillis());
      if(remaining > 0) {
        this.addon.pushNotification(
            Component.translatable("moneymaker.notification.leaderboard.title", NamedTextColor.GOLD),
            Component.translatable("moneymaker.notification.leaderboard.coolDown", NamedTextColor.RED)
        );
        return;
      }
      this.addon.apiUtil().loadLeaderboard(true);
      this.updateCoolDown = System.currentTimeMillis();
    });

    container.addChild(updateButton);

    if(!this.entries.isEmpty()) {

      if(this.sorting == SortType.USERNAME) {
        Comparator<LeaderboardEntryWidget> comparator = Comparator.comparing(entry -> entry.playerName().toLowerCase());
        if (this.sortDirection == SortDirection.DESC) {
          comparator = comparator.reversed();
        }
        this.entries.sort(comparator);
      }

      if(this.sorting == SortType.RANKING) {
        this.entries.sort(Comparator.comparing(LeaderboardEntryWidget::ranking));
        if(this.sortDirection == SortDirection.DESC) {
          Collections.reverse(this.entries);
        }
      }
      if(this.sorting == SortType.BLOCKS) {
        this.entries.sort(Comparator.comparing(LeaderboardEntryWidget::blocks));
        if(this.sortDirection == SortDirection.DESC) {
          Collections.reverse(this.entries);
        }
      }
      if(this.sorting == SortType.PICKAXE) {
        this.entries.sort(Comparator.comparing(LeaderboardEntryWidget::pickaxeRanking));
        if(this.sortDirection == SortDirection.DESC) {
          Collections.reverse(this.entries);
        }
      }
      if(this.sorting == SortType.SWORD) {
        this.entries.sort(Comparator.comparing(LeaderboardEntryWidget::swordRanking));
        if(this.sortDirection == SortDirection.DESC) {
          Collections.reverse(this.entries);
        }
      }

      ButtonWidget sortTypeButton = ButtonWidget.component(
          Component.translatable("moneymaker.ui.leaderboard.sorting.button", NamedTextColor.YELLOW)
              .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
              .append(Component.translatable("moneymaker.ui.leaderboard.header." + this.sorting.translation(), NamedTextColor.GOLD))
      ).addId("sort-type-button");
      sortTypeButton.setPressable(() -> {
        switch (this.sorting) {
          case RANKING -> this.sorting = SortType.BLOCKS;
          case BLOCKS -> this.sorting = SortType.PICKAXE;
          case PICKAXE -> this.sorting = SortType.SWORD;
          case SWORD -> this.sorting = SortType.USERNAME;
          case USERNAME -> this.sorting = SortType.RANKING;
        }
        this.reload();
      });

      container.addChild(sortTypeButton);

      ButtonWidget sortDirectionButton = ButtonWidget.component(
          Component.translatable("moneymaker.ui.leaderboard.sorting.button", NamedTextColor.YELLOW)
              .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
              .append(Component.translatable("moneymaker.ui.leaderboard.sorting." + this.sortDirection.translation(), NamedTextColor.GOLD))
      ).addId("sort-direction-button");
      sortDirectionButton.setPressable(() -> {
        switch (this.sortDirection) {
          case DESC -> this.sortDirection = SortDirection.ASC;
          case ASC -> this.sortDirection = SortDirection.DESC;
        }
        this.reload();
      });

      container.addChild(sortDirectionButton);

      HorizontalListWidget header = new HorizontalListWidget().addId("header");
      header.addEntry(ComponentWidget.i18n("moneymaker.ui.leaderboard.header.username").addId("username", (this.sorting == SortType.USERNAME ? "active" : "")));
      header.addEntry(ComponentWidget.i18n("moneymaker.ui.leaderboard.header.ranking").addId("ranking", (this.sorting == SortType.RANKING ? "active" : "")));
      header.addEntry(ComponentWidget.i18n("moneymaker.ui.leaderboard.header.blocks").addId("blocks", (this.sorting == SortType.BLOCKS ? "active" : "")));
      header.addEntry(ComponentWidget.i18n("moneymaker.ui.leaderboard.header.pickaxe").addId("pickaxe-ranking", (this.sorting == SortType.PICKAXE ? "active" : "")));
      header.addEntry(ComponentWidget.i18n("moneymaker.ui.leaderboard.header.sword").addId("sword-ranking", (this.sorting == SortType.SWORD ? "active" : "")));

      container.addChild(header);

      VerticalListWidget<LeaderboardEntryWidget> listWidget = new VerticalListWidget<>().addId("entries");
      this.entries.forEach(listWidget::addChild);
      container.addChild(new ScrollWidget(listWidget, new ListSession<>()).addId("leaderboard-scroll"));

    } else {

      container.addChild(ComponentWidget.i18n("moneymaker.ui.leaderboard.noData", NamedTextColor.DARK_RED).addId("error-text"));

    }

    this.document.addChild(container);

    this.document.addChild(Util.feedbackButton());
    this.document.addChild(Util.discordButton());
    this.document.addChild(Util.leaderboardButton());
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }

  public List<LeaderboardEntryWidget> entries() {
    return entries;
  }

  public enum SortType {
    USERNAME("username"),
    RANKING("ranking"),
    BLOCKS("blocks"),
    PICKAXE("pickaxe"),
    SWORD("sword");

    private final String translation;

    SortType(String translation) {
      this.translation = translation;
    }

    public String translation() {
      return translation;
    }

  }

  public enum SortDirection {
    ASC("asc"),
    DESC("desc");

    private final String translation;

    SortDirection(String translation) {
      this.translation = translation;
    }

    public String translation() {
      return translation;
    }

  }


}
