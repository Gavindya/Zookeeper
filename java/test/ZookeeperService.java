package test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.quorum.Observer;

public class ZookeeperService {

  ServiceProvider serviceProvider;
  private String connectionString;
  private  CuratorFramework curatorFramework;

  ZookeeperService(String connectionString){
    this.connectionString=connectionString;
  }

  protected void registerInZookeeper() {
    try{
      curatorFramework = CuratorFrameworkFactory.newClient(connectionString, new RetryNTimes(5, 1000));
      curatorFramework.start();

      ServiceDiscovery<Void> serviceDiscovery = ServiceDiscoveryBuilder
        .builder(Void.class)
        .basePath("counter")
        .client(curatorFramework).build();
      serviceDiscovery.start();

      serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName("worker").build();
      serviceProvider.start();
    }catch (Exception ex){
      ex.printStackTrace();
    }
    finally {
      curatorFramework.close();
    }
  }

  protected void update(String word){

  }

}
