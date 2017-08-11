package testCounter.Cordinator;

/**
 * Created by AdminPC on 8/9/2017.
 */
public interface ServerListener {
  void onServerEvent(ServerEvent event);
  void onCompleteEvent(CompletionEvent event);
}
