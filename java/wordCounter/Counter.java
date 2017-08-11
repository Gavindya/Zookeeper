package wordCounter;

import org.apache.zookeeper.KeeperException;

public class Counter implements Runnable {

  String[] words;
  ZookeeperService zookeeperService;

  public Counter(String[] words,ZookeeperService zookeeperService) {
    this.words = words;
    this.zookeeperService = zookeeperService;
  }

  private void Count() throws KeeperException, InterruptedException {
    for (String word : words) {
      zookeeperService.updateNode(word);
//      if (Main.result.containsKey(word)) {
//        Main.result.replace(word, Main.result.get(word) + 1);
//      }
//      else Main.result.put(word, 1l);
    }
  }

  public void run() {
    try {
      Count();
    } catch (KeeperException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
