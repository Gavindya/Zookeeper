package testCounter.Service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.zookeeper.*;
import org.apache.zookeeper.server.quorum.Observer;

import java.io.IOException;

public class ZookeeperService {

  private testCounter.Service.Counter counter;
  ServiceProvider serviceProvider;
  private static final String BASE_PATH=Constants.baseName;
  private static final String SERVICE_NAME=Constants.nodeName;
  private String connectionString;
  private  CuratorFramework client;

  ZookeeperService(String connectionString,Counter _counter) {
    this.connectionString=connectionString;
    this.counter=_counter;

  }

  protected void registerInZookeeper() {
    try{

      client = CuratorFrameworkFactory.newClient(connectionString, new RetryNTimes(5, 1000));
      client.start();
      try {
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT)
          .forPath(ZKPaths.makePath(BASE_PATH, SERVICE_NAME), counter.getServerDetails().getBytes("UTF-8"));
      }catch (KeeperException ex){

      }

    }catch (Exception ex){
//      System.out.println("error");
//      ex.printStackTrace();
    }
    finally {
      client.close();
    }
  }


}
