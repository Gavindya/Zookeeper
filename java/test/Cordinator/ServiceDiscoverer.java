package test.Cordinator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.strategies.RandomStrategy;
import test.Service.Counter;

import java.io.Closeable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gavindya Jayawardena on 8/7/2017.
 */
public class ServiceDiscoverer {

    private static final String BASE_PATH = "/counter";
    private final CuratorFramework curatorClient;
    private final ServiceDiscovery<test.Service.Counter> serviceDiscovery;
    private final ServiceProvider<test.Service.Counter> serviceProvider;
    private final List<Closeable> closeAbles = Lists.newArrayList();
    private final JsonInstanceSerializer<test.Service.Counter> serializer;
    private List<String> uris= Lists.newArrayList();

    public ServiceDiscoverer(String zookeeperAddress, String serviceName) throws Exception {
        curatorClient = CuratorFrameworkFactory.newClient(zookeeperAddress,new ExponentialBackoffRetry(1000, 3));

//        // Ideally this would be injected
        serializer = new JsonInstanceSerializer<Counter>(Counter.class); // Payload Serializer
////        serviceDiscovery = ServiceDiscoveryBuilder.builder(Counter.class).client(curatorClient).basePath(BASE_PATH).build();
        serviceDiscovery = ServiceDiscoveryBuilder.builder(Counter.class).client(curatorClient).basePath(BASE_PATH).serializer(serializer).build(); // Service Discovery
        serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).providerStrategy(new RandomStrategy<Counter>()).build(); // Service Provider for a particular service


    }


    public void start(){
        try {
            curatorClient.start();
            closeAbles.add(curatorClient);

            serviceDiscovery.start();
            closeAbles.add(0, serviceDiscovery);

            serviceProvider.start();
            closeAbles.add(0, serviceProvider);

            Thread.sleep(3000);
//            while(true){
//
//            }
//            ServiceInstance<Counter> oop = serviceProvider.getInstance();
//            System.out.println("addr "+oop);
//            Collection<ServiceInstance<Counter>> instanceCounter = serviceProvider.getAllInstances();
//            System.out.println(instanceCounter.size());
//            for(ServiceInstance<Counter> xoo : instanceCounter){
//                System.out.println(xoo.getAddress()+":"+xoo.getName()+":"+xoo.getPort());
//
//            }
            System.out.println("****");
            Collection<String> serviceNames =serviceDiscovery.queryForNames();
            System.out.println(serviceDiscovery.queryForInstances("count20").size());
            for ( String service_name: serviceNames){
                System.out.println(service_name);
                ServiceInstance<Counter> instance = serviceProvider.getInstance();
                System.out.println("instance = " +instance);
//                for ( ServiceInstance<Counter> instance : instances )
//                {
//                    System.out.println(instance.getAddress());
//                }
            }

//            Iterator<ServiceInstance<test.Service.Counter>> iterator2=serviceDiscovery.queryForInstances(BASE_PATH).iterator();
//            System.out.println(serviceDiscovery.queryForInstances("/counter/count2").size());
//            while (iterator2.hasNext()){
//                System.out.println("P:"+iterator2.next());
//            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close() {
        for (Closeable closeable : closeAbles) {
// Close all
            System.out.println(closeable);
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

//    public void query() throws Exception {
//        while (serviceProvider.getAllInstances().iterator().hasNext()){
//            System.out.println(serviceProvider.getAllInstances().iterator().next());
//        }
//    }
}
