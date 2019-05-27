import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

//May 21, 2019

public class Enemy extends Tangible {

   public Enemy(double x, double y, double width, double height, double dx, Rectangle bounds) {
      super(x, y, width, height, dx, bounds);
      
   }

   @Override
   public void drawPiece(Graphics2D g2) {
      g2.fillRect((int) x, (int) y, (int) width, (int) height);
   }

   @Override
   public void update() {
      x += dx;
   }

   @Override
   public Shape getSelfBounds() {
      return new Rectangle((int) x, (int) y, (int) width, (int) height);
   }

}
