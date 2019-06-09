import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

//May 21, 2019

public class Platform implements GamePiece {
   
   private int x, y, width, height;

   public Platform(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   @Override
   public void drawPiece(Graphics2D g2, JPanel panel) {
      g2.fill(getSelfBounds());
   }

   @Override
   public void update() {
      
   }
   
   public void playerUpdate(Player p) {
      if (this.getSelfBounds().intersects((Rectangle2D) p.getSelfBounds()) && p.getDy() > 0) {
         p.setY(y - p.getHeight());
//         p.setFalling(false);
         p.setOnPlatform(true);
         p.setFalling(false);
//         System.out.println("okay");
         System.out.println(p.isOnPlatform());
      }
      else if ((p.getX() < x || p.getX() > x + width) && p.isOnPlatform()) {
         System.out.println("bruh");
         p.setOnPlatform(false);
         p.setFalling(true);
      }
   }

   @Override
   public Shape getSelfBounds() {
      return new Rectangle(x, y, width, height);
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

}
