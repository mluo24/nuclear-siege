import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

//Miranda Luo
//May 20, 2019

public class Player extends Tangible {
   
   private double dy, jumpStart, gravity;
   private boolean left, right, jumping, falling;
   private boolean facingLeft, facingRight;
   private boolean isFiring, isHit, isInvincible, isOnPlatform;
   private double health;
   private ArrayList<Bullet> ammo;
   
   private BufferedImage[] idleSprites, jumpingSprites, movingSprites, helmetIdleSprites, helmetMovingSprites;
   private BufferedImage gunSprite;
   private Animation animation, helmetAnimation;
   
   public Player(double x, double y, double width, double height, double dx, double jumpStart, double gravity, double health, Rectangle bounds) {
      
      super(x, y, width, height, dx, bounds);
      this.jumpStart = jumpStart;
      this.gravity = gravity;
      left = right = jumping = false;
      falling = true;
      facingLeft = true;
      isHit  = isFiring = isInvincible = isOnPlatform = false;
      this.health = health;
      
      ammo = new ArrayList<Bullet>();
      
      BufferedImage sprites, helmetSprites;
            
      idleSprites = new BufferedImage[1];
      jumpingSprites = new BufferedImage[1];
      movingSprites = new BufferedImage[4];
      helmetIdleSprites = new BufferedImage[1];
      helmetMovingSprites = new BufferedImage[1];
      
      try {                
         gunSprite = ImageIO.read(new File("src/resources/weapon.png"));
         sprites = ImageIO.read(new File("src/resources/player_set.png"));
         helmetSprites = ImageIO.read(new File("src/resources/head_armor_set.png"));
      } catch (IOException ex) {
         System.out.println("hello");
         ex.printStackTrace();
         sprites = helmetSprites = gunSprite = null;
      }
            
      for (int i = 0; i < 1; i++) {
         idleSprites[i] = sprites.getSubimage(2, 1, 13, 16);
      }
      
      for (int i = 0; i < 1; i++) {
         jumpingSprites[i] = sprites.getSubimage(18, 1, 13, 16);
      }
      
      for (int i = 0; i < movingSprites.length; i++) {
         movingSprites[i] = sprites.getSubimage(13 * i + 3 * i + 2, 18, 13, 16);
      }
      
      for (int i = 0; i < helmetIdleSprites.length; i++) {
         helmetIdleSprites[i] = helmetSprites.getSubimage(4, 36, 10, 9);
      }
      
      for (int i = 0; i < helmetMovingSprites.length; i++) {
         helmetMovingSprites[i] = helmetSprites.getSubimage(11, 36, 10, 9);
      }
            
            
      animation = new Animation();
      animation.setFrames(idleSprites);
      animation.setDelay(-1);
      
      helmetAnimation = new Animation();
      helmetAnimation.setFrames(helmetIdleSprites);
      helmetAnimation.setDelay(-1);
      
   }

   public void drawPiece(Graphics2D g2, JPanel panel) {
//      g2.fillRect((int) x, (int) y, (int) width, (int) height);
      if (isFiring) {
         for (Bullet b : ammo) 
            b.drawPiece(g2, panel);
      }
      int drawWidth = (int) (facingRight ? width : -width);
      int drawX = (int) (drawWidth < 0 ? x + width : x);
      g2.drawImage(animation.getImage(), drawX, (int) y, drawWidth, (int) height, panel);
      g2.drawImage(helmetAnimation.getImage(), drawX + 1, (int) y, drawWidth - 3, 9, panel);
      g2.drawImage(gunSprite, drawX + 10, (int) y + 21, (int) (gunSprite.getWidth() * (this.getWidth() / 13.0) / 1.2), (int) ( (this.getHeight() / 16.0) * gunSprite.getHeight() / 1.5 ), panel);
   }
   
   public void update() {
      
      animation.update();
      helmetAnimation.update();
      
      
      // I stole this from your website
      if (jumping) {
         animation.setFrames(jumpingSprites);
         animation.setDelay(-1);
         dy = jumpStart;
         falling = true;
         jumping = false;
      }
      else if (falling) {
         animation.setFrames(jumpingSprites);
         animation.setDelay(-1);
         dy += gravity;
      }
      else {
         dy = 0;
      }
      y += dy;
      
      if (left) {
         animation.setFrames(movingSprites);
         animation.setDelay(100);
         x -= dx;
      }
      if (right) {
         animation.setFrames(movingSprites);
         animation.setDelay(100);
         x += dx;
      }
      
      if (!left && !right && dy == 0) {
         animation.setFrames(idleSprites);
         animation.setDelay(-1);
      }
      
      if (isOutOfBoundsX()) {
         animation.setFrames(idleSprites);
         animation.setDelay(-1);
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
//      if (isOnPlatform) {
//         falling = false;
//      }
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
      ammo.add(new Bullet(x + width / 2 - 14, y + height / 2 - 5, 28, 10, 35, bounds));
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

   public BufferedImage[] getIdleSprites() {
      return idleSprites;
   }

   public void setIdleSprites(BufferedImage[] idleSprites) {
      this.idleSprites = idleSprites;
   }

   public BufferedImage[] getJumpingSprites() {
      return jumpingSprites;
   }

   public void setJumpingSprites(BufferedImage[] jumpingSprites) {
      this.jumpingSprites = jumpingSprites;
   }

   public BufferedImage[] getMovingSprites() {
      return movingSprites;
   }

   public void setMovingSprites(BufferedImage[] movingSprites) {
      this.movingSprites = movingSprites;
   }

   public boolean isOnPlatform() {
      return isOnPlatform;
   }

   public void setOnPlatform(boolean isOnPlatform) {
      this.isOnPlatform = isOnPlatform;
   }

   public BufferedImage[] getHelmetIdleSprites() {
      return helmetIdleSprites;
   }

   public void setHelmetIdleSprites(BufferedImage[] helmetIdleSprites) {
      this.helmetIdleSprites = helmetIdleSprites;
   }

   public BufferedImage[] getHelmetMovingSprites() {
      return helmetMovingSprites;
   }

   public void setHelmetMovingSprites(BufferedImage[] helmetMovingSprites) {
      this.helmetMovingSprites = helmetMovingSprites;
   }

   public BufferedImage getGunSprite() {
      return gunSprite;
   }

   public void setGunSprite(BufferedImage gunSprite) {
      this.gunSprite = gunSprite;
   }

   public Animation getHelmetAnimation() {
      return helmetAnimation;
   }

   public void setHelmetAnimation(Animation helmetAnimation) {
      this.helmetAnimation = helmetAnimation;
   }

}
