package wordCounter;

import java.io.*;
import java.util.Arrays;

public class IO {
  private static String filepath;
  private static int numOfWords;
  private static CalculationThreadPool threadPool;

  IO(String filePath, int numberOfWords, CalculationThreadPool threadpool){
    filepath = filePath;
    numOfWords = numberOfWords;
    threadPool = threadpool;
  }
  //  public void readFile(String filepath, int numOfWords, CalculationThreadPool threadPool) throws Exception{
  public void readFile(ZookeeperService zookeeperService) throws Exception{
    BufferedReader reader = new BufferedReader(new FileReader(filepath),1024);
    String[] words = new String[numOfWords]; //String array of size specified by num of words
    int currentIndex = 0;
    int currentChar;
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

          if(currentIndex == numOfWords){
//                      System.out.println("words recognized");
            // Send array to thread pool to count n update hashMap
            System.out.println("words counted ="+numOfWords+"submitting now");
            threadPool.addWork(new Counter(words,zookeeperService));

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
      threadPool.addWork(new Counter(Arrays.copyOfRange(words, 0, currentIndex),zookeeperService));
    }



//        System.out.println("File Read Completed");
    threadPool.shutdown();
    while(!threadPool.getCountservice().isTerminated()){}
  }

//    public void writeFileTest(String filepath) throws IOException{
//        BufferedReader reader = new BufferedReader(new FileReader(filepath),8192);
//        String content;
//        do {
//            content = reader.readLine();
//        } while (content == null);
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
//        long i = 0;
//        while(i < 1000000){
//            i++;
//            writer.append(content + "\n");
//        }
//        writer.flush();
//    }
}
