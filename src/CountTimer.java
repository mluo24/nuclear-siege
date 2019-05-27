// By Miranda Luo
// Created on May 27, 2019

public class CountTimer {
   
   private long startTime;
   private long delay;
   
   private long timeElapsed;
   
   public CountTimer() {
      startTime = System.currentTimeMillis();
      timeElapsed = 0;
   }
   
   public void setDelay(long d) {
      delay = d;
   }
   
   public void update() {
      
      if(delay == -1) return;
      
      long elapsedTime = System.currentTimeMillis() - startTime;
      if (elapsedTime > delay) {
         timeElapsed++;
         this.start();
      }
            
   }
   
   public long getElapsed() {
      return timeElapsed;
   }
   
   public void start() {
      startTime = System.currentTimeMillis();
   }
   
   public void reset() {
      startTime = System.currentTimeMillis();
   }

}
