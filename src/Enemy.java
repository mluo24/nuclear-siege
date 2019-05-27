import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

//May 21, 2019

public class Enemy extends Tangible {
   
   private double health;

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
   
   public void interactUpdate(Player player, Core core) {
      if (player.getSelfBounds().intersects((Rectangle2D) this.getSelfBounds()) && player.getHealth() > 0) {
         player.setHealth(player.getHealth() - 0.25);
      }
      if (core.getSelfBounds().intersects((Rectangle2D) this.getSelfBounds()) && core.getHealth() > 0) {
         core.setHealth(core.getHealth() - 0.25);
      }
   }

   @Override
   public Shape getSelfBounds() {
      return new Rectangle((int) x, (int) y, (int) width, (int) height);
   }

}
