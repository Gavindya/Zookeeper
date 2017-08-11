package zoo;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ZooKeeperConnect {
  private static ZooKeeper zooKeeper;
  private static String parent;
  private static String child;
  private  List<String> children;

  ZooKeeperConnect(String connectString,String parentPath,String childPath){
    try {
      parent=parentPath;
      child=childPath;
      zooKeeper = new ZooKeeper(connectString, 5000, new Watcher() {
        public void process(WatchedEvent watchedEvent) {
          if(watchedEvent.getType().equals(Event.EventType.NodeDeleted)){
            System.out.println("node deleted ! ");
            try {
              leaderElection();
            } catch (KeeperException e) {
              e.printStackTrace();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      });

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void createNode(String path,String msg,String createMode) throws KeeperException, InterruptedException {
    if(zooKeeper.exists(path,false)==null) {
      zooKeeper.create(path, msg.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode.equals("p") ? CreateMode.EPHEMERAL_SEQUENTIAL : CreateMode.PERSISTENT);
    }
  }
  public void deleteNode(String path) throws KeeperException, InterruptedException {
    if(!path.substring(0,1).equals("/")){
      path="/"+path;
    }
    try{
      zooKeeper.delete(parent+path,zooKeeper.exists(parent+path,true).getVersion());
    }catch (Exception ex){
      ex.printStackTrace();
    }
    leaderElection();
  }

  public void leaderElection() throws KeeperException, InterruptedException {
    children =zooKeeper.getChildren(parent,false);
    if(children!=null){
      for(String c : children){
        System.out.println(c);
      }
      Collections.sort(children);
      System.out.println("Leader = "+children.get(0));
    }
  }

  public void createParent(String msg,String createMode) throws KeeperException, InterruptedException {
      createNode(parent,msg,createMode);
  }
  public void createChild(String msg,String createMode) throws KeeperException, InterruptedException {
    String path =parent+child;
    createNode(path,msg,createMode);
//    leaderElection();
  }
  public void deleteLeader() throws KeeperException, InterruptedException {
//    System.out.println(children.get(0));
    String leader = children.get(0);
    deleteNode(leader);
  }

  public static void main(String[] args) throws KeeperException, InterruptedException {
    String parent = "/abc";
    String candidate= "/p_";
    ZooKeeperConnect zkConnect = new ZooKeeperConnect("localhost:2182,localhost:2183,localhost:2181",parent,candidate);
    zkConnect.createParent("hello","persistent");
    zkConnect.createChild("hello1","p");
//    zkConnect.createChild("hello2","p");
//    zkConnect.createChild("hello3","p");
//    System.out.println("elect leader 1st : ");
//    zkConnect.leaderElection();
////    Thread.sleep(5000);
//    System.out.println("dlt leader 1st : ");
//    zkConnect.deleteLeader();
    Thread.sleep(100000);
  }
}
