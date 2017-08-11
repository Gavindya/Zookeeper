package wordCounter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map;

public class WriteFile extends Thread{

  @Override
  public void run() {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("src/output.txt"));
      for(Map.Entry<String,Long> entry : Main.result.entrySet()){
        writer.append(entry.getKey()+" : "+entry.getValue() + " , ");
      }
      writer.flush();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
