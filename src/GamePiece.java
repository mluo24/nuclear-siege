import java.awt.Graphics2D;
import java.awt.Shape;

//May 23, 2019

public interface GamePiece {
   
   void drawPiece(Graphics2D g2);
   
   Shape getSelfBounds();
   
   void update();

}
