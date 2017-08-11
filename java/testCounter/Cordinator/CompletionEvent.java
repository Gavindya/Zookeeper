package testCounter.Cordinator;

/**
 * Created by AdminPC on 8/10/2017.
 */
public class CompletionEvent {

  private final String server_n_sequence;

  public CompletionEvent( String server_n_sequence) {
    this.server_n_sequence = server_n_sequence;
  }

  public String getServerNSeqDetails() {
    return server_n_sequence;
  }

  @Override
  public String toString() {
    return "MembershipEvent{" +  ", server_n_sequence=" + server_n_sequence + '}';
  }
}
