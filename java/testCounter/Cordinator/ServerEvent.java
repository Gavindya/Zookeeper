package testCounter.Cordinator;

import testCounter.Service.ServerDetails;

/**
 * Created by AdminPC on 8/9/2017.
 */
public class ServerEvent {
  public enum EventType {
    ADDED, REMOVED
  }

  private final EventType eventType;
  private final String server;

  public ServerEvent(EventType eventType, String server) {
    this.eventType = eventType;
    this.server = server;
  }

  public EventType getEventType() {
    return eventType;
  }

  public String getServerDetails() {
    return server;
  }

  @Override
  public String toString() {
    return "MembershipEvent{" + "eventType=" + eventType + ", server=" + server + '}';
  }


}
