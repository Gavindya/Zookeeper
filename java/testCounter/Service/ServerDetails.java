package testCounter.Service;

import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 * Created by AdminPC on 8/8/2017.
 */
@JsonRootName("ServerDetails")
public class ServerDetails {
  private int port;
  private String address;

  public ServerDetails(int _port, String _address){
    this.port=_port;
    this.address=_address;
  }

  public String getDetails(){
    String details =address+":"+port;
    return details;
  }
  public int getPort(){
    return this.port;
  }

  public String getAddress(){
    return address;
  }

  public void setPort(int port){
    this.port=port;
  }

  public void setAddress(String address){
    this.address=address;
  }
}
