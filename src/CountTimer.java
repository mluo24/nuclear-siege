// By Miranda Luo
// Created on May 27, 2019

public class CountTimer {
   
   protected long startTime;
   protected long delay;
   protected long elapsedTime;
   protected boolean isChanged;
   protected long count;
   
   public CountTimer() {
      startTime = System.currentTimeMillis();
      count = 0;
   }
   
   public void setDelay(long d) {
      delay = d;
   }
   
   public long getDelay() {
      return delay;
   }
   
   public void update() {
      
      if(delay == -1) return;
      
      isChanged = false;
      
      elapsedTime = System.currentTimeMillis() - startTime;
      if (elapsedTime > delay) {
         count++;
         isChanged = true;
         this.reset();
      }
            
   }
   
   public long getTimeElapsed() {
      return elapsedTime;
   }
   
   public long getElapsed() {
      return count;
   }
   
   public boolean isChanged() {
      return isChanged;
   }
   
   public void start() {
      startTime = System.currentTimeMillis();
   }
   
   public void reset() {
      startTime = System.currentTimeMillis();
   }

}
