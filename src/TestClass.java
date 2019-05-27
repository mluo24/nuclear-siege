// By Miranda Luo
// Created on May 27, 2019

public class TestClass {

   public static void main(String[] args) throws InterruptedException {
      
      long startTime = System.currentTimeMillis();
      
      
      while (true) {
         
         long elapsedTime = System.currentTimeMillis() - startTime;
         long elapsedSeconds = elapsedTime / 1000;
         long secondsDisplay = elapsedSeconds % 60;
         long elapsedMinutes = elapsedSeconds / 60;
         //put here code to format and display the values
         
         long timeTillNextDisplayChange = 1000 - (elapsedTime % 1000);
         
      }

   }

}
