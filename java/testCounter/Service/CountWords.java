package testCounter.Service;

/**
 * Created by AdminPC on 8/4/2017.
 */
public class CountWords implements Runnable{
  private String[] words;

  CountWords(String[] dataRcv){
    this.words=dataRcv;
  }

  public void run() {
    for (String word : words) {
      if(Counter.results.containsKey(word)){
        Counter.results.replace(word,(Counter.results.get(word)+1));
      }else{
        Counter.results.put(word,1);
      }
    }
  }
}
