import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

//May 21, 2019

public class Enemy extends Tangible {
   
   private boolean facingLeft, facingRight;
   private double health;
   
   private BufferedImage[] movingSprites, attackingSprites;
   private Animation animation;

   public Enemy(double x, double y, double width, double height, double dx, Rectangle bounds, double health) {
      super(x, y, width, height, dx, bounds);
      this.health = health;
      
      BufferedImage sprites = null;
      
      try {                
         sprites = ImageIO.read(new File("src/resources/impsprites.png"));
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      //15
      movingSprites = new BufferedImage[8];
      attackingSprites = new BufferedImage[6];
      
      animation = new Animation();
      animation.setFrames(movingSprites);
      animation.setDelay(80); 
      
      for (int i = 0; i < movingSprites.length; i++) {
         movingSprites[i] = sprites.getSubimage(7 + i * 17 + 15 * i, 49, 15, 15);
      }
      
      for (int i = 0; i < attackingSprites.length; i++) {
         attackingSprites[i] = sprites.getSubimage(8 + i * 18 + 14 * i, 83, 14, 13);
      }
      
      if (dx < 0) {
         facingLeft = true;
         facingRight = false;
      }
      else if (dx > 0){
         facingLeft = false;
         facingRight = true;
      }
      
   }

   @Override
   public void drawPiece(Graphics2D g2, JPanel panel) {
//      g2.fillRect((int) x, (int) y, (int) width, (int) height);
      
      int drawWidth = (int) (facingRight ? width : -width);
      int drawX = (int) (drawWidth < 0 ? x + width : x);
      g2.drawImage(animation.getImage(), drawX, (int) y, drawWidth, (int) height, panel);
      
   }

   @Override
   public void update() {
      if (dx < 0) {
         facingLeft = true;
         facingRight = false;
      }
      else if (dx > 0){
         facingLeft = false;
         facingRight = true;
      }
      animation.update();
      if (dx == 0) {
         animation.setFrames(attackingSprites);
      }
      else {
         animation.setFrames(movingSprites);
      }
      x += dx;
   }
   
   public void interactUpdate(Player player, Core core) {
      if (player.getSelfBounds().intersects((Rectangle2D) this.getSelfBounds()) && player.getHealth() > 0) {
//         player.setHealth(player.getHealth() - 5);
//         if (!player.isInvincible())
            player.setHit(true);
//            if (player.isHit())
//               System.out.println(player.isHit());
      }
//      else if (!player.getSelfBounds().intersects((Rectangle2D) this.getSelfBounds())) {
//         player.setHit(false);
//         System.out.println("hello");
//      }
//      else {
//         player.setHit(false);
//         System.out.println("hello");
////         System.out.println("Hit? " + player.isHit());
//      }
      if (core.getSelfBounds().intersects((Rectangle2D) this.getSelfBounds()) && core.getHealth() > 0) {
         core.setHealth(core.getHealth() - 0.25);
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

   public boolean isFacingLeft() {
      return facingLeft;
   }

   public void setFacingLeft(boolean facingLeft) {
      this.facingLeft = facingLeft;
   }

   public boolean isFacingRight() {
      return facingRight;
   }

   public void setFacingRight(boolean facingRight) {
      this.facingRight = facingRight;
   }

   public BufferedImage[] getMovingSprites() {
      return movingSprites;
   }

   public void setMovingSprites(BufferedImage[] movingSprites) {
      this.movingSprites = movingSprites;
   }

   public BufferedImage[] getAttackingSprites() {
      return attackingSprites;
   }

   public void setAttackingSprites(BufferedImage[] attackingSprites) {
      this.attackingSprites = attackingSprites;
   }

   public Animation getAnimation() {
      return animation;
   }

   public void setAnimation(Animation animation) {
      this.animation = animation;
   }

}
