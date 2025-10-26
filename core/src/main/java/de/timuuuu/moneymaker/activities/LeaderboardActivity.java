package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.LeaderboardEntryWidget;
import de.timuuuu.moneymaker.utils.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
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

@AutoActivity
@Links({@Link("leaderboard.lss"), @Link("buttons.lss")})
public class LeaderboardActivity extends SimpleActivity {

  private MoneyMakerAddon addon;

  private SortType sorting = SortType.RANKING;
  private SortDirection sortDirection = SortDirection.ASC;

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

    ComponentWidget liveDataInfoWidget = ComponentWidget.i18n("moneymaker.ui.leaderboard.liveDataInfo").addId("liveData-info");
    container.addChild(liveDataInfoWidget);

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

      HorizontalListWidget header = new HorizontalListWidget().addId("header");

      Component username = Component.translatable("moneymaker.ui.leaderboard.header.username");
      if(this.sorting == SortType.USERNAME) {
        username.append(this.sortDirection == SortDirection.DESC ? Component.text(" ▲") : Component.text(" ▼"));
      }
      ComponentWidget usernameWidget = ComponentWidget.component(username);
      usernameWidget.setPressable(() -> {
        if(this.sorting == SortType.USERNAME) {
          if(this.sortDirection == SortDirection.ASC) {
            this.sortDirection = SortDirection.DESC;
          } else {
            this.sortDirection = SortDirection.ASC;
          }
        } else {
          this.sorting = SortType.USERNAME;
        }
        this.reload();
      });

      Component ranking = Component.translatable("moneymaker.ui.leaderboard.header.ranking");
      if(this.sorting == SortType.RANKING) {
        ranking.append(this.sortDirection == SortDirection.DESC ? Component.text(" ▲") : Component.text(" ▼"));
      }
      ComponentWidget rankingWidget = ComponentWidget.component(ranking);
      rankingWidget.setPressable(() -> {
        if(this.sorting == SortType.RANKING) {
          if(this.sortDirection == SortDirection.ASC) {
            this.sortDirection = SortDirection.DESC;
          } else {
            this.sortDirection = SortDirection.ASC;
          }
        } else {
          this.sorting = SortType.RANKING;
        }
        this.reload();
      });

      Component blocks = Component.translatable("moneymaker.ui.leaderboard.header.blocks");
      if(this.sorting == SortType.BLOCKS) {
        blocks.append(this.sortDirection == SortDirection.DESC ? Component.text(" ▲") : Component.text(" ▼"));
      }
      ComponentWidget blocksWidget = ComponentWidget.component(blocks);
      blocksWidget.setPressable(() -> {
        if(this.sorting == SortType.BLOCKS) {
          if(this.sortDirection == SortDirection.ASC) {
            this.sortDirection = SortDirection.DESC;
          } else {
            this.sortDirection = SortDirection.ASC;
          }
        } else {
          this.sorting = SortType.BLOCKS;
        }
        this.reload();
      });

      Component pickaxe = Component.translatable("moneymaker.ui.leaderboard.header.pickaxe");
      if(this.sorting == SortType.PICKAXE) {
        pickaxe.append(this.sortDirection == SortDirection.DESC ? Component.text(" ▲") : Component.text(" ▼"));
      }
      ComponentWidget pickaxeWidget = ComponentWidget.component(pickaxe);
      pickaxeWidget.setPressable(() -> {
        if(this.sorting == SortType.PICKAXE) {
          if(this.sortDirection == SortDirection.ASC) {
            this.sortDirection = SortDirection.DESC;
          } else {
            this.sortDirection = SortDirection.ASC;
          }
        } else {
          this.sorting = SortType.PICKAXE;
        }
        this.reload();
      });

      Component sword = Component.translatable("moneymaker.ui.leaderboard.header.sword");
      if(this.sorting == SortType.SWORD) {
        sword.append(this.sortDirection == SortDirection.DESC ? Component.text(" ▲") : Component.text(" ▼"));
      }
      ComponentWidget swordWidget = ComponentWidget.component(sword);
      swordWidget.setPressable(() -> {
        if(this.sorting == SortType.SWORD) {
          if(this.sortDirection == SortDirection.ASC) {
            this.sortDirection = SortDirection.DESC;
          } else {
            this.sortDirection = SortDirection.ASC;
          }
        } else {
          this.sorting = SortType.SWORD;
        }
        this.reload();
      });

      header.addEntry(usernameWidget.addId("username", (this.sorting == SortType.USERNAME ? "active" : "")));
      header.addEntry(rankingWidget.addId("ranking", (this.sorting == SortType.RANKING ? "active" : "")));
      header.addEntry(blocksWidget.addId("blocks", (this.sorting == SortType.BLOCKS ? "active" : "")));
      header.addEntry(pickaxeWidget.addId("pickaxe-ranking", (this.sorting == SortType.PICKAXE ? "active" : "")));
      header.addEntry(swordWidget.addId("sword-ranking", (this.sorting == SortType.SWORD ? "active" : "")));

      container.addChild(header);

      VerticalListWidget<LeaderboardEntryWidget> listWidget = new VerticalListWidget<>().addId("entries");
      this.entries.forEach(entry -> {
        if(entry.ranking() != 0) {
          listWidget.addChild(entry);
        }
      });
      container.addChild(new ScrollWidget(listWidget, new ListSession<>()).addId("leaderboard-scroll"));

    } else {

      container.addChild(ComponentWidget.i18n("moneymaker.ui.leaderboard.noData", NamedTextColor.DARK_RED).addId("error-text"));

    }

    this.document.addChild(container);

    this.document.addChild(Util.feedbackButton());
    this.document.addChild(Util.discordButton());
    this.document.addChild(Util.leaderboardButton());
    Util.addCredits(this.addon, this.document);
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
