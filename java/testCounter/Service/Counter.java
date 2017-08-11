package testCounter.Service;

import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Counter implements DataListener {

  private ZookeeperService zookeeperService;
  CalculationThreadPool threadPool;
  public static Map<String,Integer> results;
  private ServerDetails serverDetails;
  private DataReceiver dataReceiverThread;
  private String outputAddress;
  private int outputPort;
  private String cordinateAddress;
  private int cordinatePort;
  private String sequenceNum;
  private List listeners=new ArrayList();

  public Counter(String _connectionString){
      serverDetails=new ServerDetails(Constants.listen_port,Constants.listen_address);
      zookeeperService = new ZookeeperService(_connectionString,this);
      zookeeperService.registerInZookeeper();
      threadPool = new CalculationThreadPool(20);
      results=new HashMap<String, Integer>();
      listeners.add(this);
      dataReceiverThread = new DataReceiver(this);
      dataReceiverThread.start();
  }

  public void setData(String _data) {
    try{
      String[] dataRcvd = _data.split("::");
      System.out.println(dataRcvd[0]);
      sequenceNum = dataRcvd[0].substring(2);
      outputAddress=dataRcvd[1];
      outputPort =Integer.parseInt(dataRcvd[2]);
      cordinateAddress=dataRcvd[3];
      cordinatePort=Integer.parseInt(dataRcvd[4]);
      String[] temp = dataRcvd[5].split(","); //only data without trimmed
      String[] onlyData = new String[temp.length];
      for(int i=0;i<temp.length;i++){
        onlyData[i]=(temp[i].trim());
      }
//      System.out.println("work added");
      Future work = threadPool.addWork(new CountWords( onlyData));
      while (!work.isDone()) {
//        System.out.println("Task is not completed yet....");
        Thread.sleep(1);
      }
//      System.out.println("Task is completed, let's check result");
//      Thread.sleep(5000);

      if(results.size()!=0) {
        Socket clientSocket = new Socket(outputAddress, outputPort);
        ObjectOutputStream toServer = new ObjectOutputStream(clientSocket.getOutputStream());
        toServer.writeObject(results);
        toServer.flush();
        clientSocket.close();

        Socket cordinatorSocket = new Socket(cordinateAddress, cordinatePort);
        DataOutputStream toCordinator = new DataOutputStream(cordinatorSocket.getOutputStream());
        toCordinator.writeUTF(Constants.baseName + "/" + Constants.nodeName + ":" + sequenceNum);
        toCordinator.flush();
        cordinatorSocket.close();
        System.out.println("sent");
      }
//      threadPool.shutdown();

    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  public String getServerDetails(){
    return this.serverDetails.getDetails();
  }

  public static void main(String[] args) {
      try{
        String _connectionString = "localhost:2181,localhost:2182,localhost:2183";
        Counter counter = new Counter(_connectionString);
//          Thread.sleep(30000);



//          for(Map.Entry<String ,Integer> entry : counter.results.entrySet()){
//            System.out.println(entry.getKey() +":"+entry.getValue());
//          }


      }catch (Exception ex){
          ex.printStackTrace();
      }
  }

  public void onEvent(ReceivedEvent event) {
    System.out.println("EVENT OCCURED = "+event.getEventType());
    setData(event.getData());
  }

  public void trigger(ReceivedEvent event){
    System.out.println("***********"+event.getEventType());
    Iterator i = listeners.iterator();
    while (i.hasNext()){
      ((DataListener) i.next()).onEvent(event);
    }
  }
}
