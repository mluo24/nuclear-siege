import java.awt.Rectangle;

//May 23, 2019

public abstract class Tangible implements GamePiece {
   
   protected double x, y, width, height;
   protected double dx, moveSpeed;
   // protected BufferedImage sprites[]
   protected Rectangle bounds;
   
   public Tangible(double x, double y, double width, double height, double moveSpeed, Rectangle bounds) {
      super();
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.moveSpeed = moveSpeed;
      this.dx = moveSpeed;
      this.bounds = bounds;
   }

   public Tangible(double x, double y, double width, double height, Rectangle bounds) {
      super();
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.bounds = bounds;
      this.moveSpeed = 6;
      this.dx = moveSpeed;
   }
   
   public boolean isOutOfBounds() {
      return y + height > bounds.getHeight() || y < 0 || x + width > bounds.getWidth() || x < 0;
   }

   public double getX() {
      return x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getWidth() {
      return width;
   }

   public void setWidth(double width) {
      this.width = width;
   }

   public double getHeight() {
      return height;
   }

   public void setHeight(double height) {
      this.height = height;
   }

   public double getDx() {
      return dx;
   }

   public void setDx(double dx) {
      this.dx = dx;
   }
   
   public double getMoveSpeed() {
      return dx;
   }

   public void setMoveSpeed(double moveSpeed) {
      this.moveSpeed = moveSpeed;
   }

   public Rectangle getBounds() {
      return bounds;
   }

   public void setBounds(Rectangle bounds) {
      this.bounds = bounds;
   }

}
