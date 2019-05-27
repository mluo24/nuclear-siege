import java.awt.image.*;

public class Animation {
   
   private BufferedImage[] frames;
   private int currentFrame;
   
   private long startTime;
   private long delay;
   
   private boolean timeUp = false;
   
   public Animation() {
      startTime = System.nanoTime();
   }
   
   public void setFrames(BufferedImage[] images) {
      frames = images;
      if(currentFrame >= frames.length) currentFrame = 0;
   }
   
   public void setDelay(long d) {
      delay = d;
   }
   
   public void start() {
      startTime = System.nanoTime();
   }
   
   public void update() {
      if(delay == -1) return;
      
      long elapsed = (System.nanoTime() - startTime) / 1000000;
      if (elapsed > delay) {
//         currentFrame++;
         timeUp = true;
         startTime = System.nanoTime();
//         timeUp = false;
      }
//      if(currentFrame == frames.length) {
//         currentFrame = 0;
//      }
      
   }
   
   public void reset() {
      startTime = System.nanoTime();
   }
   
   public boolean isTimeUp() {
      return timeUp; 
   }
   
   public void setTimeUp(boolean timeUp) {
      this.timeUp = timeUp;
   }
   
   public BufferedImage getImage() {
      return frames[currentFrame];
   }
   
}