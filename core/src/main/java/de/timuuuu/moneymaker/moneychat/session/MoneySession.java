package de.timuuuu.moneymaker.moneychat.session;

import net.labymod.api.client.session.Session;
import net.labymod.api.util.UUIDHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class MoneySession implements Session {

  private final String username;
  private final UUID uniqueId;
  private final boolean hasUniqueId;
  private final String accessToken;
  private final Session.Type type;
  private final boolean premium;

  public MoneySession(String username, UUID uniqueId, String accessToken, Session.Type type) {
    this.username = username;
    this.uniqueId = uniqueId == null ? UUIDHelper.createUniqueId(username) : uniqueId;
    this.hasUniqueId = uniqueId != null;
    this.accessToken = accessToken;
    this.type = type;
    this.premium = isPremium(accessToken);
  }

  @Override
  public @NotNull String getUsername() {
    return username;
  }

  @Override
  public UUID getUniqueId() {
    return uniqueId;
  }

  @Override
  public boolean hasUniqueId() {
    return hasUniqueId;
  }

  @Override
  public @Nullable String getAccessToken() {
    return accessToken;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public boolean isPremium() {
    return premium;
  }

  private boolean isPremium(String accessToken) {
    return accessToken != null && accessToken.length() > 10;
  }

}
