import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// I stole this from your website
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
   
   private static final long serialVersionUID = 1L;
   
   // constants
   private static final int PREF_W = 1000;
   private static final int PREF_H = 600;
   private static final int GROUND = PREF_H - 90;
   
   // panel utilities
   private RenderingHints hints = new RenderingHints(
   RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
   private Font font = new Font("Cooper Black", Font.PLAIN, 40);
   
   // game elements
   private Timer timer;
   private Player player;
   private Core core;
   private ArrayList<Enemy> enemies;
   private ArrayList<Obstacle> obstacles;
   private int score;
   private CountTimer scoreKeeper;
   private CountTimer enemyTimer;
   private CountTimer invincibility;
   
   /**
    * Constructs a new GamePanel
    */
   public GamePanel() {
      
      timer = new Timer(10, this);
      timer.start();
      
      player = new Player(PREF_W / 2 - 50, GROUND / 2 - 50, 100, 100, 10, -35, 1.75, 50, new Rectangle(PREF_W, GROUND));
      core = new Core(PREF_W / 2 - 50, GROUND - 150, 100, 150, 0, new Rectangle(PREF_W, GROUND), 100);
      enemies = new ArrayList<Enemy>();
      for (int i = 0; i < 1; i++) {
         addEnemy();
      }
      obstacles = new ArrayList<Obstacle>();
      score = 0;
      scoreKeeper = new CountTimer();
      scoreKeeper.setDelay(50);
      enemyTimer = new CountTimer();
      enemyTimer.setDelay(500);
      invincibility = new CountTimer();
      invincibility.setDelay(1000);
            
      this.setFocusable(true);
      this.addKeyListener(this);
      this.setBackground(Color.WHITE);
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
      
//      reset();
      
   }

   @Override
   protected void paintComponent(Graphics g) {
      
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHints(hints);
      g2.setFont(font);
      g2.setColor(Color.RED);
      
      //PAINT TO THE PANEL HERE
      g2.drawString("Score: " + score, 0, 100);
      g2.drawString("Core health: " + core.getHealth(), 280, 300);
      g2.drawString("Player health: " + player.getHealth(), 220, 200);
      
      g2.setColor(Color.BLUE);
      
      core.drawPiece(g2);
      
      g2.setColor(Color.GREEN);
      player.drawPiece(g2);
      
      g2.setColor(Color.RED);
      for (Enemy enemy : enemies) {
         enemy.drawPiece(g2);
      }
      
      if (isGameOver()) {
         super.paintComponent(g); 
         g2.drawString("Game Over!", PREF_W / 2 - 100, GROUND / 2 - 20);
         g2.drawString("Final Score: " + score, PREF_W / 2 - 100, GROUND / 2 + 20);
      }

   }
   
   private void update() {
      if (!isGameOver()) {
         scoreKeeper.update();
         enemyTimer.update();
         for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
            enemies.get(i).interactUpdate(player, core);
            if (enemies.get(i).getHealth() <= 0) {
               enemies.remove(i);
            }
         }
//         int count = 0;
//         if (enemyTimer.isChanged()) {
//            while (count < 2) {
//               addEnemy();
//               count++;
//            }
//         }
         if (player.isHit() && !player.isInvincible()) { //&& !player.isInvincible()
            
            player.setInvincible(true);
             //(!player.isInvincible()) {
               player.setHealth(player.getHealth() - 5);
//               System.out.println("hello");
//               System.out.println("lowering health");
               
            
         }
         if (player.isInvincible()) {
            System.out.println(player.isHit());
            invincibility.update();
//            player.setHit(false);
//            System.out.println("updating");
            if (invincibility.isChanged())
               player.setInvincible(false);
         }
         score = (int) scoreKeeper.getElapsed();
         player.update();
      }
      repaint();
   }
   
   private boolean isGameOver() {
      return player.getHealth() == 0 || core.getHealth() == 0;
   }
   
   private void reset() {
      
   }
   
   private void addEnemy() {
      int assign = ( (int) (Math.random() * 2) ) == 0 ? 0 - ((int) (Math.random() * 10) + 50) : PREF_W + (int) (Math.random() * 10);
      enemies.add(new Enemy(assign, GROUND - 140, 50, 140, (Math.random() * 3) + 2, new Rectangle(PREF_W, GROUND), 10));
      Enemy latest = enemies.get(enemies.size() - 1);
      if (latest.getX() >= GROUND) latest.setDx(-latest.getMoveSpeed());
   }
   
   public void saveHighScore() {
      
   }
   
   public void getHighScore() {
      
   }
   
   @Override
   public void keyTyped(KeyEvent e) {
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_UP && !player.isFalling()) {
         player.setJumping(true);
//         player.setFalling(false);
      }
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
         player.fire();
//         System.out.println(player.getAmmo());
      }
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
         player.setLeft(true);
         player.setFacingLeft(true);
         player.setFacingRight(false);
      }
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
         player.setRight(true);
         player.setFacingLeft(false);
         player.setFacingRight(true);
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
//      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//         player.setJumping(false);
//         player.setFalling(true);
//      }
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
         player.setLeft(false);
      }
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
         player.setRight(false);
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      update();
   } 

   @Override
   public void mousePressed(MouseEvent e) {
//      System.out.println("PRESSING");
      System.out.println("I: " + player.isInvincible());
      System.out.println("H: " + player.isHit());
   }

   @Override
   public void mouseReleased(MouseEvent e)
   {
   }

   @Override
   public void mouseDragged(MouseEvent e)
   {
//      System.out.println("DRAGGING");
   }

   @Override
   public void mouseMoved(MouseEvent e)
   {
   }

   @Override
   public void mouseClicked(MouseEvent e){}

   @Override
   public void mouseEntered(MouseEvent e){}

   @Override
   public void mouseExited(MouseEvent e){}
   
   //run methods

   private static void createAndShowGUI() {
      GamePanel gamePanel = new GamePanel();

      JFrame frame = new JFrame("Frame Template");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(gamePanel);
      frame.pack();
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGUI();
         }
      });
   }

}
