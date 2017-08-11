package testCounter.Output;

import testCounter.Service.ServerDetails;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by AdminPC on 8/10/2017.
 */
public class Reciever extends Thread {
  private ServerSocket socket;
  private String clientSentence;
  private GenerateOutput generateOutput;

  Reciever(GenerateOutput generateOutput) {
    try {
      this.socket = new ServerSocket(9999);
      this.generateOutput=generateOutput;
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
        ObjectInputStream mapInputStream = new ObjectInputStream(inFromClient);
        Map<String, Integer> recievedMap = (Map) mapInputStream.readObject();

        generateOutput.trigger(new ReceivedEvent(ReceivedEvent.EventType.ADDED,recievedMap));

        DataInputStream stringInput = new DataInputStream(inFromClient);
        try {
          String receive = stringInput.readUTF();
          System.out.println("RECEIVED++"+receive);
        }catch (Exception ex){

        }
//        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//        clientSentence = inFromClient.readLine();
//        System.out.println("Received: " + clientSentence);
//        String[] data = clientSentence.split(":");
//        GenerateOutput.data.put(Integer.parseInt(data[0]),data[1]);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
