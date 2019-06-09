import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JPanel;

//May 24, 2019

public class Core extends Tangible {
   
   private double health;

   public Core(double x, double y, double width, double height, double dx, Rectangle bounds, double health) {
      super(x, y, width, height, dx, bounds);
      this.health = health;
   }

   @Override
   public void drawPiece(Graphics2D g2, JPanel panel) {
      g2.fillRect((int) x, (int) y, (int) width, (int) height); 
   }

   @Override
   public void update() {
   }
   
   public double getHealth() {
      return health;
   }

   public void setHealth(double health) {
      this.health = health;
   }

   @Override
   public Shape getSelfBounds() {
      return new Rectangle((int) x, (int) y, (int) width, (int) height);
   }

}
