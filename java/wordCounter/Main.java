package wordCounter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Main {
  private static String inputFilePath = "src/test5.txt";
  public static ConcurrentMap<String, Long> result = new ConcurrentHashMap<String, Long>();

  public static void main(String[] args) throws Exception {
    CalculationThreadPool threadPool = new CalculationThreadPool(20);
    IO io = new IO(inputFilePath, 1000, threadPool);
    String parent = "/parent";
    String candidate= "/p_";
    ZookeeperService zk = new ZookeeperService("localhost:2182,localhost:2183,localhost:2181",parent,candidate);
    zk.createParent("counter","p");
//    Long start = System.currentTimeMillis();
    io.readFile(zk);
//    Long end = System.currentTimeMillis();
//    System.out.println(end - start);
//    printAll();
    zk.printAll();
    Thread.sleep(3600000);

//    Thread.sleep(3600000);
  }

  public static void printAll(){
    WriteFile writeFile = new WriteFile();
    writeFile.start();
  }
}
