import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

//May 21, 2019

public class Enemy extends Tangible {
   
   private double health;

   public Enemy(double x, double y, double width, double height, double dx, Rectangle bounds, double health) {
      super(x, y, width, height, dx, bounds);
      this.health = health;
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
      if (player.getSelfBounds().intersects((Rectangle2D) this.getSelfBounds()) && player.getHealth() > 0 && !player.isInvincible()) {
//         player.setHealth(player.getHealth() - 5);
         player.setHit(true);
//         System.out.println("Hit? " + player.isHit());
      }
      if (core.getSelfBounds().intersects((Rectangle2D) this.getSelfBounds()) && core.getHealth() > 0) {
//         core.setHealth(core.getHealth() - 1);
         dx = 0;
      }
      for (int i = 0; i < player.getAmmo().size(); i++) {
         Bullet b = player.getAmmo().get(i);
         if (b.getSelfBounds().intersects((Rectangle2D)this.getSelfBounds())) {
            health = 0;
            player.getAmmo().remove(i);
         }
      }
   }

   @Override
   public Shape getSelfBounds() {
      return new Rectangle((int) x, (int) y, (int) width, (int) height);
   }

   public double getHealth() {
      return health;
   }

   public void setHealth(double health) {
      this.health = health;
   }

}
