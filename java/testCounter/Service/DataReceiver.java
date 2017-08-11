package testCounter.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by AdminPC on 8/8/2017.
 */
public class DataReceiver extends Thread{

  String clientSentence;
  Counter counter;
  ServerSocket welcomeSocket;

  DataReceiver(Counter _counter){
    try{
      this.counter = _counter;
      this.welcomeSocket = new ServerSocket(Constants.listen_port);
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }
  public void run(){
    while (true) {
      System.out.println("listening...");
      Socket connectionSocket = null;
      try {
        connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        clientSentence = inFromClient.readLine();
        System.out.println("Received: " + clientSentence);
        if(clientSentence.getBytes().length>10){
          this.counter.trigger(new ReceivedEvent(ReceivedEvent.EventType.ADDED,clientSentence));
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
