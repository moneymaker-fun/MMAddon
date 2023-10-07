package de.timuuuu.moneymaker.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class WebSocketClient {

  private Socket socket;

  public WebSocketClient(String host, int port) throws IOException {
    socket = new Socket(host, port);
  }

  public void connect() throws IOException {
    // Send the WebSocket handshake message
    OutputStream outputStream = socket.getOutputStream();
    outputStream.write("GET / HTTP/1.1\r\n".getBytes());
    outputStream.write("Upgrade: websocket\r\n".getBytes());
    outputStream.write("Connection: Upgrade\r\n".getBytes());
    outputStream.write("\r\n".getBytes());

    // Read the WebSocket handshake response
    InputStream inputStream = socket.getInputStream();
    byte[] response = new byte[1024];
    int length = inputStream.read(response);
    String responseHeader = new String(response, 0, length);

    if (!responseHeader.contains("Upgrade: websocket")) {
      throw new IOException("Server does not support WebSockets");
    }
  }

  public void sendMessage(String message) throws IOException {
    OutputStream outputStream = socket.getOutputStream();
    outputStream.write((byte) 0x81);
    outputStream.write((byte) message.length());
    outputStream.write(message.getBytes());
    outputStream.flush();
  }

  public String receiveMessage() throws IOException {
    InputStream inputStream = socket.getInputStream();
    byte[] message = new byte[1024];
    int length = inputStream.read(message);

    if (message[0] == (byte) 0x81) {
      return new String(message, 1, length - 1);
    } else {
      throw new IOException("Unexpected message type");
    }
  }

  public void close() throws IOException {
    socket.close();
  }

  public static void main(String[] args) throws IOException {
    WebSocketClient client = new WebSocketClient("78.31.64.201", 3000);
    client.connect();
    client.sendMessage("Hello, world!");
    String message = client.receiveMessage();
    System.out.println(message);
    client.close();
  }
}