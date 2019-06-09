import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JPanel;

//May 23, 2019

public interface GamePiece {
   
   void drawPiece(Graphics2D g2, JPanel panel);
   
   Shape getSelfBounds();
   
   void update();

}
