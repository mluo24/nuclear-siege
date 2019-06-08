import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

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
   public void drawPiece(Graphics2D g2) {
      g2.fill(getSelfBounds());
   }

   @Override
   public void update() {
      
   }

   @Override
   public Shape getSelfBounds() {
      return new Rectangle(x, y, width, height);
   }

}
