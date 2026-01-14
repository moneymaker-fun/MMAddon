package de.timuuuu.moneymaker.moneychat.protocol.packets;

import com.google.gson.JsonElement;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MoneyPacketAddonMessage extends MoneyPacket {

  private String key;
  private byte[] data;

  public MoneyPacketAddonMessage(String key, byte[] data) {
    this.key = key;
    this.data = data;
  }

  public MoneyPacketAddonMessage(String key, String json) {
    this.key = key;
    this.data = this.toBytes(json);
  }

  public MoneyPacketAddonMessage(String key, JsonElement element) {
    this(key, element.toString());
  }

  public MoneyPacketAddonMessage() {
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.key = packetBuffer.readString();
    byte[] data = new byte[packetBuffer.readInt()];
    packetBuffer.readBytes(data);
    this.data = data;
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeString(this.key);
    packetBuffer.writeInt(this.data.length);
    packetBuffer.writeBytes(this.data);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String getKey() {
    return key;
  }

  public String getJson() {
    try {
      StringBuilder outStr = new StringBuilder();
      if (this.data != null && this.data.length != 0) {
        if (this.isCompressed(this.data)) {
          try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(this.data));
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
              outStr.append(line);
            }
          }
        } else {
          outStr.append(Arrays.toString(this.data));
        }

        return outStr.toString();
      } else {
        return "";
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private byte[] toBytes(String in) {
    byte[] str = in.getBytes(StandardCharsets.UTF_8);

    try {
      if (str.length == 0) {
        return new byte[0];
      } else {
        try (ByteArrayOutputStream obj = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(obj)) {
          gzip.write(str);
          gzip.finish();
          return obj.toByteArray();
        }
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private boolean isCompressed(byte[] compressed) {
    return compressed[0] == 31 && compressed[1] == -117;
  }

}
