package testCounter.Cordinator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.zookeeper.ZooKeeper;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import testCounter.Service.ServerDetails;

import java.io.Closeable;
import java.util.List;

public class ServiceDiscoverer extends Thread {

    private static final String BASE_PATH = "/counter";
    private final CuratorFramework curatorClient;
//    private final ServiceDiscovery<Void> serviceDiscovery;
//    private final ServiceProvider<String> serviceProvider;
    private final List<Closeable> closeAbles = Lists.newArrayList();
//    private final JsonInstanceSerializer<String> serializer;
    private List<String> uris= Lists.newArrayList();
    private ZooKeeper zooKeeper;
    private final PathChildrenCache pathChildrenCache;
  private FileLoader fileLoader;

    public ServiceDiscoverer(String zookeeperAddress, FileLoader fileLoader) throws Exception {
        zooKeeper = new ZooKeeper(zookeeperAddress,3000,null);
        curatorClient = CuratorFrameworkFactory.newClient(zookeeperAddress,new ExponentialBackoffRetry(1000, 3));
        this.fileLoader=fileLoader;
//        serializer = new JsonInstanceSerializer<String>(String.class); // Payload Serializer
//////        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServerDetails.class).client(curatorClient).basePath(BASE_PATH).build();
//        serviceDiscovery = ServiceDiscoveryBuilder.builder(Void.class).client(curatorClient).basePath(BASE_PATH).build(); // Service Discovery
//        serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).providerStrategy(new RandomStrategy<String>()).build(); // Service Provider for a particular service
      pathChildrenCache = new PathChildrenCache(curatorClient,BASE_PATH,true);
      startDiscovery();

    }


    public void startDiscovery(){
        try {
            curatorClient.start();
            closeAbles.add(curatorClient);

//            serviceDiscovery.start();
//            closeAbles.add(0, serviceDiscovery);
//
//            serviceProvider.start();
//            closeAbles.add(0, serviceProvider);

            Thread.sleep(1000);
//            while(true){
//
//            }
//            ServiceInstance<ServerDetails> oop = serviceProvider.getInstance();
//            System.out.println("addr "+oop);
//            Collection<ServiceInstance<ServerDetails>> instanceServerDetails = serviceProvider.getAllInstances();
//            System.out.println(instanceServerDetails.size());
//            for(ServiceInstance<ServerDetails> xoo : instanceServerDetails){
//                System.out.println(xoo.getAddress()+":"+xoo.getName()+":"+xoo.getPort());
//
//            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


  public void run() {
//    while (true) {
      //get available service n put it in the list
//      System.out.println("****");
//      Collection<String> serviceNames = null;
      try {
//        Stat stat =zooKeeper.exists("/counter/count2",true);
//        System.out.println("1--->"+stat);
//        if(stat!=null){
//          System.out.println(new String(zooKeeper.getData("/counter/count2",false,null)));
//        }else{
//          System.out.println("2-->"+stat);
//        }

//        Collection<String> serviceNames = serviceDiscovery.queryForNames();
//        System.out.println(serviceDiscovery.queryForInstances("count20").size());
//        for (String service_name : serviceNames) {
//          System.out.println(service_name);
//          Collection<ServiceInstance<Void>> instances = serviceDiscovery.queryForInstances(service_name);
//          Collection<ServiceInstance<String>> instances = serviceProvider.getAllInstances();
//          System.out.println("instance = " + instance);
//          System.out.println(instances.size());
//          for ( ServiceInstance<Void> i : instances )
//          {
//            System.out.println("i : ");
//              System.out.println(i);
//          }
//        }
//        if(!FileLoader.serversList.contains(new ServerDetails(7182,"localhost"))){
//          FileLoader.serversList.add(new ServerDetails(7182,"localhost"));
//          System.out.println("added");
//          return;
//        }

        pathChildrenCache.getListenable().addListener((PathChildrenCacheListener) new PathChildrenCacheListener() {
          public void childEvent(CuratorFramework cf, PathChildrenCacheEvent evt) throws Exception {
            switch (evt.getType()) {
              case INITIALIZED: {
//                    List<Object> members = evt.getInitialData().stream().collect(Collectors.toList());
                System.out.println("initialized");
                break;
              }
              case CHILD_ADDED: {
                System.out.println("added");
                if(!new String(evt.getData().getData()).equals(null)) {
//                  System.out.println(new String(evt.getData().getData()));
                  fileLoader.serversList.put(evt.getData().getPath(), new String(evt.getData().getData()));
                  fileLoader.trigger(new ServerEvent(ServerEvent.EventType.ADDED,new String(evt.getData().getData())));
                  System.out.println("processed");
                }
                break;
              }
              case CHILD_REMOVED: {
                System.out.println("removed");
                fileLoader.serversList.remove(evt.getData().getPath());
                fileLoader.trigger(new ServerEvent(ServerEvent.EventType.REMOVED,(evt.getData().getPath()+"-"+new String(evt.getData().getData()))));
                break;
              }
//              case CHILD_UPDATED: {
//                System.out.println("updated");
//                if(!new String(evt.getData().getData()).equals(null)) {
//                  if(fileLoader.serversList.containsKey(evt.getData().getPath())){
//                    fileLoader.changeServer(evt.getData().getPath(),new String(evt.getData().getData()));
//                  }
//                }
//                fileLoader.trigger(new ServerEvent(ServerEvent.EventType.UPDATED,new String(evt.getData().getData())));
//
//                break;
//              }
              default:
                break;
            }
          }
        });
        try {
          this.pathChildrenCache.start();
        } catch (Exception ex) {
          ex.printStackTrace();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
//    }
  }

//    public void query() throws Exception {
//        while (serviceProvider.getAllInstances().iterator().hasNext()){
//            System.out.println(serviceProvider.getAllInstances().iterator().next());
//        }
//    }

  public void close() {
    for (Closeable closeable : closeAbles) {
// Close all
      System.out.println(closeable);
    }
  }
  private ServerDetails makeServer(String serverDetailsStr){
    System.out.println("in make server");
    if(serverDetailsStr!=""){
      String[] details;
      try{
        details = serverDetailsStr.split(":");
        ServerDetails sd = new ServerDetails(Integer.parseInt(details[0]),details[1]);
        System.out.println("asfdjsklgkghks"+sd);
        return sd;

      }catch (Exception ex){
        return null;

      }
    }else{
      return null;
    }
  }


  public ServiceInstance<Void> getServiceUrl() {
    try {
      return null;
//            return serviceProvider.getInstance();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }



}
