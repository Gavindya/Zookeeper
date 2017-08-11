package testCounter.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CalculationThreadPool {
  private ExecutorService countservice;

  public CalculationThreadPool(int threadCount) {

    this.countservice = Executors.newFixedThreadPool(threadCount);
  }

  public ExecutorService getCountservice() {
    return countservice;
  }

  public Future addWork(Runnable runnable){
    Future work =countservice.submit(runnable);
    return work;
  }

  public void shutdown(){
    countservice.shutdown();
  }
}
