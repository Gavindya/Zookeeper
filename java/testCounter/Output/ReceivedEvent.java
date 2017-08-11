package testCounter.Output;

import java.util.Map;

public class ReceivedEvent {
  public enum EventType {
    ADDED
  }

  private final EventType eventType;
  private final Map<String,Integer> dataRecieved;

  public ReceivedEvent(EventType eventType, Map<String,Integer> data) {
    this.eventType = eventType;
    this.dataRecieved = data;
  }

  public EventType getEventType() {
    return eventType;
  }

  public Map<String, Integer> getData() {
    return dataRecieved;
  }

  @Override
  public String toString() {
    return "DataRcvdEvnt{" + "eventType=" + eventType + ", data len=" + dataRecieved.size() + '}';
  }

}
