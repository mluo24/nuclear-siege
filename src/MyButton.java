import java.awt.Graphics2D;
import java.awt.Shape;

//Jun 7, 2019

public class MyButton implements GamePiece {
   
   private int x, y, width, height;
   private String message;

   public MyButton(int x, int y, int width, int height, String message) {
      super();
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.message = message;
   }

   public int getX() {
      return x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getWidth() {
      return width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return height;
   }

   public void setHeight(int height) {
      this.height = height;
   }
   
   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   @Override
   public void drawPiece(Graphics2D g2) {
      g2.drawString(message, x, y);
   }

   @Override
   public Shape getSelfBounds() {
      return null;
   }

   @Override
   public void update() {
   }

}
