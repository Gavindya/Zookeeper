package test;

import org.apache.zookeeper.ZooKeeper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Counter {

  private ZookeeperService zookeeperService;
  private byte[] data;
  CalculationThreadPool threadPool;
  public Map<String,Integer> results;

  public Counter(String _connectionString){
      zookeeperService = new ZookeeperService(_connectionString);
      zookeeperService.registerInZookeeper();
      threadPool = new CalculationThreadPool(20);
      results=new HashMap<String, Integer>();
  }

  //Imlement Listener
  //on receive of data
  public void processData(){
    String dataStr = new String(data);
    String[] dataRcvd = dataStr.substring(1,dataStr.length()-1).split(",");
    threadPool.addWork(new CountWords(dataRcvd,this));
//    System.out.println(Arrays.toString(x));
//    String d = Arrays.toString(x).substring(1,Arrays.toString(x).length()-1);
//    System.out.println(d);
//    String[] y = d.split(",");
//    for(int v=0;v<y.length;v++){
//      System.out.println(y[v]);
//    }

  }

  public static void main(String[] args) {
    String _connectionString = "localhost:2181";
    Counter counter = new Counter(_connectionString);
  }
}
