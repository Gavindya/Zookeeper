package test.Service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.quorum.Observer;

public class ZookeeperService {

  private Counter counter;
  ServiceProvider serviceProvider;
  private static final String BASE_PATH="/counter";
  private static final String SERVICE_NAME="count20";
  private String connectionString;
  private  CuratorFramework client;

  ServiceDiscovery<Counter> serviceDiscovery;
  ServiceInstance<Counter> thisInstance;

  ZookeeperService(String connectionString,Counter _counter){
    this.connectionString=connectionString;
    this.counter=_counter;

  }

  protected void registerInZookeeper() {
    try{
      client = CuratorFrameworkFactory.newClient(connectionString, new RetryNTimes(5, 1000));
      client.start();
      JsonInstanceSerializer<Counter> serializer = new JsonInstanceSerializer<Counter>(Counter.class);
//
//      ServiceDiscovery<Counter> serviceDiscovery = ServiceDiscoveryBuilder
//        .builder(Counter.class)
//        .basePath(BASE_PATH).serializer(serializer)
//        .client(client).build();
//      System.out.println(serviceDiscovery.toString());
//      serviceDiscovery.start();
//
//      serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(SERVICE_NAME).build();
//      System.out.println(serviceProvider.toString());
//      serviceProvider.start();
        // in a real application, you'd have a convention of some kind for the URI layout
        UriSpec uriSpec = new UriSpec("localhost:{port}");
//
        thisInstance = ServiceInstance.<Counter>builder()
                .address("localhost").name(SERVICE_NAME).port(7182).uriSpec(uriSpec).payload(counter).build();

      System.out.println(thisInstance);
//
//        // if you mark your payload class with @JsonRootName the provided JsonInstanceSerializer will work
//
        serviceDiscovery = ServiceDiscoveryBuilder.builder(Counter.class)
                .client(client)
                .basePath(BASE_PATH)
                .serializer(serializer)
                .thisInstance(thisInstance)
                .build();

      serviceDiscovery.start();
      System.out.println("service discovery started");
    }catch (Exception ex){
      System.out.println("error");
      ex.printStackTrace();
    }
    finally {
      client.close();
    }
  }

  protected void update(String word){

  }

}
