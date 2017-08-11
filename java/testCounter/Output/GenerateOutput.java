package testCounter.Output;

import java.util.*;

public class GenerateOutput implements DataListener {
  private Map<String,Integer> sorted = new HashMap<String, Integer>();
  private List listeners=new ArrayList();

  GenerateOutput(){
    listeners.add(this);
  }

  public void onEvent(ReceivedEvent event) {
    System.out.println("***************************trigger implmnt************************");
    for(Map.Entry<String,Integer> entry : event.getData().entrySet()){
      if(sorted.containsKey(entry.getKey())){
        int count = sorted.get(entry.getKey());
        sorted.replace(entry.getKey(),(entry.getValue()+count));
      }else{
        sorted.put(entry.getKey(),entry.getValue());
      }
    }

    for(Map.Entry<String,Integer> e : sorted.entrySet()){
      System.out.println(e.getKey()+":"+e.getValue());
    }
    System.out.println("----------------------------------------------------------------");

  }

  public void trigger(ReceivedEvent event){
    System.out.println("************************************************"+event.getEventType());
    Iterator i = listeners.iterator();
    while (i.hasNext()){
      ((DataListener) i.next()).onEvent(event);
    }
  }

  public static void main(String[] args) {
    GenerateOutput generateOutput = new GenerateOutput();
    Reciever reciever = new Reciever(generateOutput);
    reciever.start();
  }

}
