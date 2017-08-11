package testCounter.Cordinator;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Receiver extends Thread {
  private ServerSocket socket;
  private  String clientMsg;
  private FileLoader fileLoader;

  Receiver(FileLoader fileLoader) {
    try {
      this.socket = new ServerSocket(9555);
      this.fileLoader = fileLoader;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  public void run() {
    while (true) {
      System.out.println("listening...");
      Socket connectionSocket = null;
      try {
        connectionSocket = socket.accept();
        InputStream inFromClient = connectionSocket.getInputStream();// InputStream from where to receive the map, in case of network you get it from the Socket instance.
        DataInputStream inputStream = new DataInputStream(inFromClient);
        clientMsg = inputStream.readUTF();
        System.out.println("CLIENT MSG RECEIVED----"+clientMsg);
        fileLoader.triggerCompletion(new CompletionEvent(clientMsg));

//        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//        clientSentence = inFromClient.readLine();
//        System.out.println("Received: " + clientSentence);
//        String[] data = clientSentence.split(":");
//        GenerateOutput.data.put(Integer.parseInt(data[0]),data[1]);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
