package testCounter.Cordinator;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FileLoader implements ServerListener{
  private String inputFilePath;
  private int byteStreamSize;
//  private int numOfWords;
  private ConcurrentMap<Integer,String> bufferedData= new ConcurrentHashMap<Integer, String>();
  protected ConcurrentMap<String,String> serversList = new ConcurrentHashMap<String, String>();
  private ConcurrentMap<String,String> workers = new ConcurrentHashMap<String, String>();
  private int progress =0;
  private int sequence =0;
  private static List listeners = new ArrayList();

  public FileLoader(String path,int byteStreamSize){
    this.inputFilePath=path;
    this.byteStreamSize =byteStreamSize;
//    numOfWords=100; //for now. change this
    listeners.add(this);
  }

  protected void process(String node) {
    try{

      System.out.println("progress = "+progress);

      System.out.println("processing file");
      BufferedReader reader = new BufferedReader(new FileReader(inputFilePath),1024);
      //*********
      reader.skip(progress);
//      String[] words = new String[numOfWords]; //String array of size specified by num of words
      ArrayList<String> words = new ArrayList<String>();
      int currentChar;
      byte[] dataStream;
      int currentIndex = 0;
      int current_progress =0;
      StringWriter tempWord = new StringWriter();

      // terminate when end of file reached
      while(current_progress*2!=byteStreamSize) {
        if((currentChar = reader.read()) != -1){
          progress=progress+1;
          current_progress=current_progress+1;

          if (currentChar != ' ' && currentChar != ',' && currentChar != '/' && currentChar != '\\'
            && currentChar != '.' && currentChar != ';' && currentChar != ':' && currentChar != '-'
            && currentChar != '#' && currentChar != '@' && currentChar != '%' && currentChar != '^'
            && currentChar != '*' && currentChar != '~' && currentChar != '`' && currentChar != '='
            && currentChar != '+' && currentChar != '"' && currentChar != '|'
            && currentChar != '_' &&currentChar != '\t' && currentChar != '(' && currentChar != ')'
            && currentChar != '<' && currentChar != '>' && currentChar != '!' && currentChar != '?'
            && currentChar != '1' && currentChar != '2'&& currentChar != '3' &&currentChar != '4'
            && currentChar != '5' && currentChar != '6'&& currentChar != '7' &&currentChar != '8'
            && currentChar != '9' && currentChar != '0'  && currentChar != '\n' && currentChar != '$') {
            tempWord.append((char)currentChar);
          } else {
            if(!tempWord.toString().trim().isEmpty()) {
  //            words[currentIndex] = tempWord.toString().trim();
              words.add(tempWord.toString().trim());
              currentIndex++;
              tempWord = new StringWriter(); //always clear writer once added to words string array

              int size=0;
              for(String s :words) {
                if(s!=null) size=size+s.getBytes().length;
                else break;
              }
  //            if(size==byteStreamSize){
  //              sequence=sequence+1;
  //              String listString = String.join(", ", words);
  ////              sendToServer(node,Arrays.toString(words));
  //              sendToServer(node,listString);
  //              System.out.println("submitting now :: "+listString);
  //
  ////              words = new String[numOfWords];
  //              words = new ArrayList<String>();
  //              currentIndex = 0;
  //            }
            }
          }
        }else{
          sendBufferedData();
          break;
        }
      }

      char nextChar = (char) reader.read();
      if (nextChar != ' ' && nextChar != ',' && nextChar != '/' && nextChar != '\\'
        && nextChar != '.' && nextChar != ';' && nextChar != ':' && nextChar != '-'
        && nextChar != '_' &&nextChar != '\t' && nextChar != '(' && nextChar != ')'
        && nextChar != '<' && nextChar != '>' && nextChar != '!'&&nextChar != '?'
        && nextChar != '1' && nextChar != '2'&& nextChar != '3' &&nextChar != '4'
        && nextChar != '5' && nextChar != '6'&& nextChar != '7' &&nextChar != '8'
        && nextChar != '9' && nextChar != '0'  && nextChar != '\n' && nextChar != '$'
        && nextChar != '#' && nextChar != '@' && nextChar != '%' && nextChar != '^'
        && nextChar != '*' && nextChar != '~' && nextChar != '`' && nextChar != '='
        && nextChar != '+'
        ) {
        sequence=sequence+1;
        String listString = String.join(",", words);
        if(!listString.equals("")) {
          sendToServer(node, listString);
        }
//        System.out.println("submitting now 1");
      }else{
        words.add(tempWord.toString().trim());
        sequence=sequence+1;
        String listString = String.join(",", words);
        sendToServer(node,listString);
//        System.out.println("submitting now 2");
      }
      // Send remaining words to thread pool as well
//      if(!tempWord.toString().trim().isEmpty()) {
////        words[currentIndex] = tempWord.toString().trim();
//        words.add(tempWord.toString().trim());
//        currentIndex++;
//      }
//
//      //when exit while loop without getting required number of words
//      if(currentIndex > 0){
//        sequence=sequence+1;
//        String listString = String.join(", ", words);
//        sendToServer(node,listString);
//        System.out.println("submitting now :: "+listString);
////        sendToServer(node,Arrays.toString(words));
////        System.out.println("words counted ="+numOfWords+"submitting now :: "+Arrays.toString(words));
//      }
////        System.out.println("File Read Completed");
////    threadPool.shutdown();
////    while(!threadPool.getCountservice().isTerminated()){}
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  private void sendBufferedData(){
    System.out.println("end of file reached");
    try{
      Thread.sleep(3000);
      if(bufferedData.size()!=0){
        for (Map.Entry<Integer,String> entry : bufferedData.entrySet()){
          String available = getFreeServer();
          if(available!=null){
            sendToServer(available,entry.getKey().toString(),entry.getValue());
//            String data=entry.getKey()+"::localhost::9999::localhost::9555::"+entry.getValue();
//            workers.put(available,String.valueOf(entry.getKey()));
//            String server = serversList.get(available);
//            String[] details =  server.split(":");
//            Socket clientSocket = new Socket(details[0], Integer.parseInt(details[1]));
//            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//            outToServer.writeUTF(data);
//            outToServer.flush();
//            clientSocket.close();
//            System.out.println("SENT TO "+available+" --> "+data);
          }
        }
      }else{
        Thread.sleep(2000);
        Socket clientSocket = new Socket(InetAddress.getLocalHost(), 9999);
        DataOutputStream toOutputGenerator = new DataOutputStream(clientSocket.getOutputStream());
        toOutputGenerator.writeUTF("DONE");
        toOutputGenerator.flush();
        clientSocket.close();
      }
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  public void sendToServer(String node,String sequenceNumber,String data){
    try{
      data=sequenceNumber+"::localhost::9999::localhost::9555::"+data;
      workers.put(node,sequenceNumber);
      String server = serversList.get(node);
      String[] server_details =  server.split(":");
      Socket clientSocket = new Socket(server_details[0], Integer.parseInt(server_details[1]));
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      outToServer.writeUTF(data);
      outToServer.flush();
      clientSocket.close();
      System.out.println("RE--SENT TO "+node+" --> "+data);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  public void sendToServer(String node,String data) {
    try {
      bufferedData.put(sequence,data);
      data=sequence+"::localhost::9999::localhost::9555::"+data;
      workers.put(node,String.valueOf(sequence));
      String server = serversList.get(node);
      String[] details =  server.split(":");
      Socket clientSocket = new Socket(details[0], Integer.parseInt(details[1]));
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      System.out.println("****"+data);
      outToServer.writeUTF(data);
      outToServer.flush();
      clientSocket.close();
      System.out.println("SENT TO "+node+" --> "+data);

//      for(Map.Entry<Integer,String> e : bufferedData.entrySet()){
//        System.out.println("seq:"+e.getKey()+":--data--:"+e.getValue());
//      }
//      for(Map.Entry<String,String> e : workers.entrySet()){
//        System.out.println("node :"+e.getKey()+":--seq--:"+e.getValue());
//      }
//      for(Map.Entry<String,String> e : serversList.entrySet()){
//        System.out.println("node :"+e.getKey()+":--address--:"+e.getValue());
//      }
    }catch (Exception ex){
      ex.printStackTrace();
    }


  }

  public void changeServer(String node,String serverDetails){
    if(serversList.containsKey(node)){
      serversList.replace(node,serverDetails);
    }
  }

  public void triggerCompletion(CompletionEvent event){
    System.out.println("completion event triggered");
    Iterator i = listeners.iterator();
    while (i.hasNext()){
      System.out.println(i.getClass());
      ((ServerListener) i.next()).onCompleteEvent(event);
    }
  }

  public void onCompleteEvent(CompletionEvent event){
    try{
      String[] details = event.getServerNSeqDetails().split(":");
      String node = details[0];
      String seq = details[1];
      if(workers.containsKey(node) && workers.get(node).equals(seq) && bufferedData.containsKey(Integer.parseInt(seq))){
        workers.remove(node);
  //      System.out.println(workers.size());
  //      for(Map.Entry<String,String> e : workers.entrySet()){
  //        System.out.println("node :"+e.getKey()+":--seq--:"+e.getValue());
  //      }
        bufferedData.remove(Integer.parseInt(seq));
  //      for(Map.Entry<Integer,String> e : bufferedData.entrySet()){
  //        System.out.println("seq:"+e.getKey()+":--data--:"+e.getValue());
  //      }
  //      System.out.println(bufferedData.size());
  //      //workers = node / seq
  //      //buffered data = seq / data
  //      if(serversList.containsKey(node)){
  //        if(bufferedData.size()!=0){
  //          Map.Entry<Integer,String> bufferedDataEntry = bufferedData.entrySet().iterator().next();
  //          int seqNum = bufferedDataEntry.getKey();
  //          String data = bufferedDataEntry.getValue();
  //          sendToServer(node,data);
  //          if(workers.containsValue(String.valueOf(seqNum))){
  //            for(Map.Entry<String ,String > worker : workers.entrySet()){
  //              if(worker.getValue().equals(String.valueOf(seqNum))){
  //                workers.remove(worker.getKey());
  //                workers.put(node,String.valueOf(seqNum));
  //                break;
  //              }
  //            }
  //          }
  //        }else{
            Thread.sleep(20);
          if(serversList.containsKey(node) && (!workers.containsKey(node))){
            process(node);
          }
//          String available =getFreeServer();
//          if(available!=null){  //workers = node / seq || buffered data = seq / data
//            int tempSequence;
//            if(workers.containsKey(node)){
//              tempSequence = Integer.parseInt(workers.get(node)); //worker might not be here
//            }
//
//            if(bufferedData.size()>0){
//              for(Map.Entry<Integer,String> bufferEntry : bufferedData.entrySet()){
//                if(bufferEntry.getKey()!=tempSequence){
//
//                }
//              }
//            }
//          }
        }

    }catch (Exception ex){
      ex.printStackTrace();
    }
  }
//  }
  public void trigger(ServerEvent event){
    Iterator i = listeners.iterator();
    while (i.hasNext()){
      ((ServerListener) i.next()).onServerEvent(event);
    }
  }

  public void onServerEvent(ServerEvent event) {
    System.out.println("event"+event);
    try{
      switch (event.getEventType()) {
        case ADDED: {
          System.out.println(serversList.containsValue(event.getServerDetails()));
          for (Map.Entry<String, String> entry : serversList.entrySet()) {
            if (entry.getValue().equals(event.getServerDetails())) {
              System.out.println(entry.getKey() + ":" + entry.getValue());
              process(entry.getKey());
              break;
            }
          }
          break;
        }
        case REMOVED: {
          //assign to a new server in llist  which is not in workers
          System.out.println(event.getServerDetails());
          String[] details = event.getServerDetails().split("-");
          String node = details[0];
//workers = node / seq
//buffered data = seq / data
          if (workers.containsKey(node)) {
            for(Map.Entry<String,String> worker : workers.entrySet()){
              if (worker.getKey().equals(node)) {
                String seqNum = workers.get(node);
                String newNode = getFreeServer();
                if(newNode!=null){
                  sendToServer(newNode,seqNum,bufferedData.get(Integer.parseInt(seqNum)));
//                  String data=seqNum+"::localhost::9999::localhost::9555::"+bufferedData.get(Integer.parseInt(seqNum));
//                  workers.put(newNode,seqNum);
//                  String server = serversList.get(newNode);
//                  String[] server_details =  server.split(":");
//                  Socket clientSocket = new Socket(server_details[0], Integer.parseInt(server_details[1]));
//                  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//                  outToServer.writeUTF(data);
//                  outToServer.flush();
//                  clientSocket.close();
//                  System.out.println("RE--SENT TO "+newNode+" --> "+data);
                }
              }
            }
          }
          break;
        }
//        case UPDATED: {
//          System.out.println("updated");
//          //when updated check if exist in workers. then update
//          break;
//        }
        default:
          System.out.println("default");
          break;
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public String getFreeServer(){
    for(Map.Entry<String,String> server : serversList.entrySet()){
      if(!workers.containsKey(server.getKey())){
        return server.getKey();
      }
    }
    return null;
  }

  public static void main(String[] args) {
    try{
      FileLoader fileLoader=new FileLoader("src/test5.txt",1000);
      ServiceDiscoverer serviceDiscoverer = new ServiceDiscoverer("localhost:2181,localhost:2182,localhost:2183",fileLoader);
      serviceDiscoverer.start();
      Receiver receiver = new Receiver(fileLoader);
      receiver.start();

    }catch (Exception ex){
      ex.printStackTrace();
    }
  }
}
