package testCounter.Service;

import java.util.Map;

/**
 * Created by AdminPC on 8/10/2017.
 */
public class ReceivedEvent {
  public enum EventType {
    ADDED
  }

  private final EventType eventType;
  private final String dataRecieved;

  public ReceivedEvent(EventType eventType,String data) {
    this.eventType = eventType;
    this.dataRecieved = data;
  }

  public EventType getEventType() {
    return eventType;
  }

  public String getData() {
    return dataRecieved;
  }

  @Override
  public String toString() {
    return "DataRcvdEvnt{" + "eventType=" + eventType + ", data len=" + dataRecieved + '}';
  }

}
