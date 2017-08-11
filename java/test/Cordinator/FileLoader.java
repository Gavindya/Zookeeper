package test.Cordinator;

import java.io.*;
import java.util.Arrays;

public class FileLoader {
  private String inputFilePath;
  private int byteStreamSize;
  private int numOfWords;

  public FileLoader(String path,int byteStreamSize){
    this.inputFilePath=path;
    this.byteStreamSize =byteStreamSize;
    numOfWords=100; //for now. change this
  }

  protected void process() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(inputFilePath),1024);
    String[] words = new String[numOfWords]; //String array of size specified by num of words
    int currentIndex = 0;
    int currentChar;
    byte[] dataStream;
    StringWriter tempWord = new StringWriter();

    // terminate when end of file reached
    while((currentChar = reader.read()) != -1) {
      if (currentChar != ' ' && currentChar != ',' && currentChar != '/' && currentChar != '\\'
        && currentChar != '.' && currentChar != ';' && currentChar != ':' && currentChar != '-'
        && currentChar != '_' &&currentChar != '\t' && currentChar != '(' && currentChar != ')'
        && currentChar != '<' && currentChar != '>'
        && currentChar != '1' && currentChar != '2'&& currentChar != '3' &&currentChar != '4'
        && currentChar != '5' && currentChar != '6'&& currentChar != '7' &&currentChar != '8'
        && currentChar != '9' && currentChar != '0') {
        tempWord.append((char)currentChar);
      } else {
        if(!tempWord.toString().trim().isEmpty()) {
          words[currentIndex] = tempWord.toString().trim();
          currentIndex++;
          tempWord = new StringWriter(); //always clear writer once added to words string array

          int size=0;
          for(String s :words) {
            if(s!=null) size=size+s.getBytes().length;
            else break;
          }
        if(currentIndex == numOfWords || size==byteStreamSize){
              // Send array to thread pool to count n update hashMap
            dataStream= new byte[Arrays.toString(words).getBytes().length];
            dataStream = Arrays.toString(words).getBytes();
            System.out.println("words counted ="+numOfWords+"submitting now :: "+Arrays.toString(words));
              //send to byte sender. UDP would do fine

              words = new String[numOfWords];
              currentIndex = 0;
            }
          }
        }
      }
    // Send remaining words to thread pool as well
    if(!tempWord.toString().trim().isEmpty()) {
        words[currentIndex] = tempWord.toString().trim();
        currentIndex++;
    }

    //when exit while loop without getting required number of words
    if(currentIndex > 0){
      dataStream= new byte[Arrays.toString(words).getBytes().length];
      dataStream =Arrays.toString(words).getBytes();
      System.out.println("words counted ="+numOfWords+"submitting now :: "+Arrays.toString(words));
//      threadPool.addWork(new Counter(Arrays.copyOfRange(words, 0, currentIndex),zookeeperService));
    }



//        System.out.println("File Read Completed");
//    threadPool.shutdown();
//    while(!threadPool.getCountservice().isTerminated()){}
  }

  public static void main(String[] args) {
    try{
      FileLoader fileLoader=new FileLoader("src/test.txt",1000);
//      fileLoader.process();
      ServiceDiscoverer serviceDiscoverer = new ServiceDiscoverer("localhost:2181,localhost:2182,localhost:2183","count20");
//      ServiceDiscoverer serviceDiscoverer = new ServiceDiscoverer("localhost:2181,localhost:2182,localhost:2183","counter");
        serviceDiscoverer.start();
//        serviceDiscoverer.query();
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }
}
