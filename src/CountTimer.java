// By Miranda Luo
// Created on May 27, 2019

//derived from the animation class
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
            
      isChanged = false;
      
      elapsedTime = System.currentTimeMillis() - startTime;
      if (elapsedTime > delay) {
         count++;
         isChanged = true;
         this.start();
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
      elapsedTime = 0;
      startTime = System.currentTimeMillis();
   }

}
