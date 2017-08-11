package zoo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LeaderElection {

  public static void main(String[] args) throws IOException {

//    if(args.length < 2) {
//      System.err.println("Usage: java -jar <jar_file_name> <process id integer> <zkhost:port pairs>");
//      System.exit(2);
//    }

    final int id = 60028;
    final String zkURL = "localhost:2181";
//    final int id = Integer.parseInt(args[0]);
//    final String zkURL = args[1];

    final ExecutorService service = Executors.newSingleThreadExecutor();

    final Future<?> status = service.submit(new ProcessNode(id, zkURL));

    try {
      status.get();
    } catch (InterruptedException e) {
      service.shutdown();
    }catch (ExecutionException e) {
      service.shutdown();
    }
  }
}
