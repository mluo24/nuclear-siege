// By Miranda Luo
// Created on May 27, 2019

public class EnemyTimer extends CountTimer {
   
   public void update() {
      
      elapsedTime = System.currentTimeMillis() - startTime;
      
      if (elapsedTime > delay) {
         
         this.reset();
      }
   }
   
}
