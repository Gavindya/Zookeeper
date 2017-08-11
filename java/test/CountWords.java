package test;

/**
 * Created by AdminPC on 8/4/2017.
 */
public class CountWords implements Runnable{
  private String[] words;
  private Counter counter;

  CountWords(String[] dataRcv,Counter counter){
    this.words=dataRcv;
    this.counter=counter;
  }

  public void run() {
    for (String word : words) {
      if(counter.results.containsKey(word)){
        counter.results.replace(word,(counter.results.get(word)+1));
      }else{
        counter.results.put(word,1);
      }
    }
  }
}
