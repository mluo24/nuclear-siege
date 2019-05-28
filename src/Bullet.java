import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

//May 21, 2019

public class Bullet extends Tangible {
   
   public Bullet(double x, double y, double width, double height, double dx, Rectangle bounds) {
      super(x, y, width, height, dx, bounds);
   }

   @Override
   public void drawPiece(Graphics2D g2) {
      g2.fillOval((int) x, (int) y, (int) width, (int) height);
   }

   @Override
   public void update() {
      x += dx;
   }

   @Override
   public String toString() {
      return "Bullet [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", dx=" + dx + ", bounds="
            + bounds + "]";
   }

   @Override
   public Shape getSelfBounds() {
      return new Ellipse2D.Double((int) x, (int) y, (int) width, (int) height);
   }

}
