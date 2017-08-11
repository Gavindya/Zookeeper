package test;

import wordCounter.Counter;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FileLoader {
  private String inputFilePath;
  private int byteStreamSize;
  private int numOfWords;

  public FileLoader(String path,int byteStreamSize){
    this.inputFilePath=path;
    this.byteStreamSize =byteStreamSize;
    numOfWords=1000; //for now. change this
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
        && currentChar != '_' &&currentChar != '\t'
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
            size=size+s.getBytes().length;
          }
//        if(currentIndex == numOfWords){
          if(size==byteStreamSize){
              // Send array to thread pool to count n update hashMap
            dataStream= new byte[Arrays.toString(words).getBytes().length];
            dataStream = Arrays.toString(words).getBytes();
            System.out.println("words counted ="+numOfWords+"submitting now :: "+dataStream);
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
      System.out.println("words counted ="+numOfWords+"submitting now :: "+dataStream);
//      threadPool.addWork(new Counter(Arrays.copyOfRange(words, 0, currentIndex),zookeeperService));
    }



//        System.out.println("File Read Completed");
//    threadPool.shutdown();
//    while(!threadPool.getCountservice().isTerminated()){}
  }
}
