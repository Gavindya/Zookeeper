package wordCounter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZookeeperService {

  private static ZooKeeper zooKeeper;
  private static String parent;
  private static String child;
  private  List<String> children;

  ZookeeperService(String connectString,String parentPath,String childPath){
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
      zooKeeper.create(path, msg.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode.equals("e") ? CreateMode.EPHEMERAL: CreateMode.PERSISTENT);
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

  public void updateNode(String path){
    try {
      System.out.println("word received = " + path);
      children = zooKeeper.getChildren(parent, true);
      if (children.size() != 0) {
        for (String aChildren : children) {
          if (aChildren.contains(path)) {
            byte[] val = zooKeeper.getData(parent + "/" + aChildren, false, zooKeeper.exists(parent + "/" + aChildren, false));
            int occurance = Integer.parseInt(new String(val));
            zooKeeper.setData(parent + "/" + aChildren, String.valueOf(occurance + 1).getBytes(), zooKeeper.exists(parent + "/" + aChildren, false).getVersion());
          }else{
            createNode((parent + "/" + path), "1", "e");
          }
        }
      } else {
        createNode((parent + "/" + path), "1", "e");
      }
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  public void printAll() throws KeeperException, InterruptedException {
    children =zooKeeper.getChildren(parent,false);
    if(children.size()!=0){
      Collections.sort(children);
      for(String c : children){
        System.out.println(c+":"+new String(zooKeeper.getData((parent+"/"+c),false,zooKeeper.exists((parent+"/"+c),false))));
      }
    }
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

}
