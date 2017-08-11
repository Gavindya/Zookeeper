package test.Service;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.schema.JsonSerializableSchema;

import java.util.HashMap;
import java.util.Map;

@JsonRootName("Counter")
public class Counter {

  private ZookeeperService zookeeperService;
  private byte[] data;
  CalculationThreadPool threadPool;
    @JsonProperty
  public Map<String,Integer> results;

  public Counter(String _connectionString){
      zookeeperService = new ZookeeperService(_connectionString,this);
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
      try{
        String _connectionString = "localhost:2181,localhost:2182,localhost:2183";
        Counter counter = new Counter(_connectionString);
          Thread.sleep(120000);
      }catch (Exception ex){
          ex.printStackTrace();
      }
  }
}
