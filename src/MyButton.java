import java.awt.*;

import javax.swing.JPanel;

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
   public void drawPiece(Graphics2D g2, JPanel panel) {
      FontMetrics fm = g2.getFontMetrics();
      int msgY = y + fm.getHeight() - fm.getLeading();
      int msgX = fm.stringWidth(message) >= width ? x : x + (width / 2 - fm.stringWidth(message) / 2) ;
      g2.setColor(Color.LIGHT_GRAY);
      g2.fill(getSelfBounds());
      g2.setColor(Color.BLACK);
      g2.drawString(message, msgX, msgY);
   }

   @Override
   public Shape getSelfBounds() {
      return new Rectangle(x, y, width, height);
   }

   @Override
   public void update() {
   }

   @Override
   public String toString() {
      return "MyButton [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", message=" + message + "]";
   }

}
