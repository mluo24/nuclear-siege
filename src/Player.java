import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

//Miranda Luo
//May 20, 2019

public class Player extends Tangible {
   
   private double dy, jumpStart, gravity;
   private boolean left, right, jumping, falling;
   private boolean facingLeft, facingRight;
   private boolean isFiring, isHit, isInvincible;
   private double health;
   private ArrayList<Bullet> ammo;
   
   public Player(double x, double y, double width, double height, double dx, double jumpStart, double gravity, double health, Rectangle bounds) {
      
      super(x, y, width, height, dx, bounds);
      this.jumpStart = jumpStart;
      this.gravity = gravity;
      left = right = jumping = false;
      falling = true;
      isHit  = isFiring = isInvincible = false;
      this.health = health;
      
      ammo = new ArrayList<Bullet>();
      
   }

   public void drawPiece(Graphics2D g2) {
      g2.fillRect((int) x, (int) y, (int) width, (int) height);
      if (isFiring) {
         for (Bullet b : ammo) 
            b.drawPiece(g2);
      }
   }
   
   public void update() {
      
      // I stole this from your website
      if (jumping) {
         dy = jumpStart;
         falling = true;
         jumping = false;
      }
      else if (falling) {
         dy += gravity;
      }
      else {
         dy = 0;
      }
      y += dy;
      
      if (left) {
         x -= dx;
      }
      if (right) {
         x += dx;
      }
      
      if (isOutOfBoundsX()) {
         if (left) {
            left = false;
            x = 0;
         }
         else if (right) {
            right = false;
            x = bounds.getWidth() - width;
         }
      }
      
      if (isOutOfBounds()) {
         falling = false;
         returnY();
      }
      if (ammo.isEmpty()) {
         isFiring = false;
      }
      if (isFiring) {
         for (Bullet b : ammo) {
              b.update();
         }
      }
      
      for (int i = 0; i < ammo.size(); i++) {
         if (ammo.get(i).isOutOfBounds()) {
            ammo.remove(i);
         }
      }
      
      if (health < 0) health = 0;
      
   }
   
   public void fire() {
      isFiring = true;
      ammo.add(new Bullet(x + width / 2 - 10, y + height / 2 - 10, 20, 20, 35, bounds));
      if (facingLeft) {
         ammo.get(ammo.size() - 1).dx = -ammo.get(ammo.size() - 1).moveSpeed;
      }
      if (facingRight) {
         ammo.get(ammo.size() - 1).dx = ammo.get(ammo.size() - 1).moveSpeed;
      }
   }
   
   public void returnY() {
      y = bounds.getHeight() - height;
   }
   
   public boolean isOutOfBounds() {
      return y + height > bounds.getHeight();
   }
   
   public boolean isOutOfBoundsX() {
      return x < 0 || x + width > bounds.getWidth();
   }

   public double getDy() {
      return dy;
   }

   public void setDy(double dy) {
      this.dy = dy;
   }

   public double getJumpStart() {
      return jumpStart;
   }

   public void setJumpStart(double jumpStart) {
      this.jumpStart = jumpStart;
   }

   public double getGravity() {
      return gravity;
   }

   public void setGravity(double gravity) {
      this.gravity = gravity;
   }

   public boolean isLeft() {
      return left;
   }

   public void setLeft(boolean left) {
      this.left = left;
   }

   public boolean isRight() {
      return right;
   }

   public void setRight(boolean right) {
      this.right = right;
   }

   public boolean isJumping() {
      return jumping;
   }

   public void setJumping(boolean jumping) {
      this.jumping = jumping;
   }

   public boolean isFalling() {
      return falling;
   }

   public void setFalling(boolean falling) {
      this.falling = falling;
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

   public ArrayList<Bullet> getAmmo() {
      return ammo;
   }

   public void setAmmo(ArrayList<Bullet> ammo) {
      this.ammo = ammo;
   }

   @Override
   public Shape getSelfBounds() {
      return new Rectangle((int) x, (int) y, (int) width, (int) height);
   }

   public boolean isFiring() {
      return isFiring;
   }
	
   public void setFiring(boolean isFiring) {
      this.isFiring = isFiring;
   }
	
   public boolean isHit() {
      return isHit;
   }

   public void setHit(boolean isHit) {
      this.isHit = isHit;
   }

   public boolean isInvincible() {
      return isInvincible;
   }

   public void setInvincible(boolean isInvincible) {
      this.isInvincible = isInvincible;
   }

   public double getHealth() {
      return health;
   }
	
   public void setHealth(double health) {
      this.health = health;
   }

}
