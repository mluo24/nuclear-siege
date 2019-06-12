import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// I stole this from your website
// Main game runner
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
   
   private static final long serialVersionUID = 1L;
   
   // constants
   private static final int PREF_W = 1200;
   private static final int PREF_H = 600;
   private static final int GROUND = PREF_H - 110;
   private static final int TITLE_SCREEN = 0;
   private static final int PLAYING = 1;
   private static final int PAUSED = 2;
   private static final int INSTRUCTIONS = 3;
   
   // panel utilities
//   private RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//   private Font font = new Font("m5x7", Font.PLAIN, 40);
   private Font font = new Font("m5x7", Font.PLAIN, 40);
   private Font titleFont = new Font("m5x7", Font.PLAIN, 100);
   private Font countdownFont = new Font("Verdana", Font.PLAIN, 250);
   private String highScoreFile = "src/highscore.txt";
   private MyButton[] utilities = {new MyButton(PREF_W / 2 - 50, PREF_H / 2 + 10, 100, 50, "Play"), 
         new MyButton(PREF_W / 2 - 100, PREF_H / 2 + 100, 200, 50, "How to Play"),
         new MyButton(PREF_W / 2 - 50, PREF_H / 2 + 190, 100, 50, "Exit")};
   private MyButton[] ending = {new MyButton(PREF_W / 2 - 50, PREF_H / 2 + 10, 100, 50, "Restart"), 
         new MyButton(PREF_W / 2 - 50, PREF_H / 2 + 100, 100, 50, "Title")};
   private MyButton back = new MyButton(50, 50, 100, 50, "Back");
   private BufferedImage titleBg, background;
   
   // game elements
   private int gameState = TITLE_SCREEN;
   private Timer timer;
   private Player player;
   private Core core;
   private ArrayList<Enemy> enemies;
   private Platform[] platforms = 
      {new Platform(0, PREF_H / 2, PREF_W / 2 - 250, 20), 
            new Platform(PREF_W - (PREF_W / 2 - 250), PREF_H / 2, PREF_W / 2 - 250, 20)};
   private int score;
   private int highScore;
   private double maxPlayerHealth = 50;
   private double maxCoreHealth = 100;
   private int countdownNum;
   private CountTimer countdown;
   private CountTimer scoreKeeper;
   private CountTimer enemyTimer;
   private CountTimer invincibility;
   
   /**
    * Constructs a new GamePanel
    */
   public GamePanel() {
      
      try {                
         titleBg = ImageIO.read(new File("src/resources/bricks.png"));
         background = ImageIO.read(new File("src/resources/back.png"));
      } catch (IOException ex) {
           ex.printStackTrace();
      }
 
      this.setFocusable(true);
      this.addKeyListener(this);
      this.setBackground(Color.WHITE);
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
            
      reset();
      
   }

   @Override
   protected void paintComponent(Graphics g) {

      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
//      g2.setRenderingHints(hints);
      g2.setFont(font);
      g2.setColor(Color.WHITE);
      
      // font metrics
      String message = "";
      FontMetrics fm = g2.getFontMetrics();
      int msgWidth = fm.stringWidth(message);
      int msgHeight = fm.getHeight();
      int messageX = PREF_W / 2 - msgWidth / 2;
      int messageY = PREF_H / 2 - msgHeight / 2;
      
      
      //PAINT TO THE PANEL HERE
      if (gameState == TITLE_SCREEN) {
                  
         g2.drawImage(titleBg, 0, 0, PREF_W, PREF_H, this);
         for (MyButton b : utilities) {
            b.drawPiece(g2, this);
         }
         
         g2.setFont(titleFont);
         g2.setColor(Color.WHITE);
         
         message = "NUCLEAR";
         fm = g2.getFontMetrics();
         msgHeight = fm.getHeight();
         msgWidth = fm.stringWidth(message);
         messageX = PREF_W / 2 - msgWidth / 2;
         g2.drawString(message, messageX , messageY - 100);
         
         message = "SIEGE";
         msgWidth = fm.stringWidth(message);
         messageX = PREF_W / 2 - msgWidth / 2;
         
         g2.setFont(titleFont);
         g2.drawString(message, messageX , messageY);
         
//         for (int i = 0; i < enemies.get(0).getAttackingSprites().length; i++) {
//            BufferedImage img = enemies.get(0).getAttackingSprites()[i];
//            g2.drawImage(img, (img.getWidth() * 4 + 20) * i, 10, img.getWidth() * 4, img.getHeight() * 4, this);
//         }
         
      }
      
      if (gameState == INSTRUCTIONS) {
         g2.drawImage(titleBg, 0, 0, PREF_W, PREF_H, this);
         showInstructions(g2);
         back.drawPiece(g2, this);
      }

      // top UI
      if (gameState == PLAYING || gameState == PAUSED) {
         
         super.paintComponent(g);
         g2.drawImage(background, 0, 0, PREF_W, PREF_H, this);
         message = "High Score: " + highScore;
         msgWidth = fm.stringWidth(message);
         g2.drawString("Score: " + score, 10, 40);
         g2.drawString(message, PREF_W - msgWidth - 10, 40);
         g2.drawString("Core health:", 10, 80);
//         g2.drawString("Player health: " + player.getHealth(), 220, 200);
         
         
         // draw ground
//         g2.setColor(Color.BLACK);
//         g2.fillRect(0, GROUND, PREF_W, PREF_H - GROUND);
         
         // draw platforms
         for (Platform p : platforms) {
            p.drawPiece(g2, this);
         }
         
         // draw core
         g2.setColor(Color.BLUE);
         core.drawPiece(g2, this);
         
         // draw enemies
         g2.setColor(Color.YELLOW);
         for (Enemy enemy : enemies) {
            enemy.drawPiece(g2, this);
         }
         
         // draw player
//         if (player.isInvincible()) {
//            g2.setColor(Color.CYAN);
//   //         g2.drawString("poof", (int) player.getX(), (int) player.getY());
//         }
//         else {
//            g2.setColor(Color.GREEN);
//         }
         player.drawPiece(g2, this);
         
         // draw health bars
         g2.setColor(Color.WHITE);
         // player
         g2.drawRect((int) player.getX(), (int) player.getY() - 20, (int) player.getWidth(), 10);
         
         g2.setColor(Color.RED);
         g2.fillRect((int) player.getX(), (int) player.getY() - 20, (int) ((player.getHealth() / maxPlayerHealth) * (player.getWidth())) , 10);
         g2.setColor(Color.BLACK);
         g2.fillRect((int) player.getX() + (int) ((player.getHealth() / maxPlayerHealth) * (player.getWidth())), (int) player.getY() - 20, (int) ((player.getWidth()) - ((player.getHealth() / maxPlayerHealth) * (player.getWidth()))) , 10);
         
         // core
         g2.setColor(Color.WHITE);
         g2.drawRect(10, 90, PREF_W - 20, 15);
         g2.setColor(Color.RED);
         g2.fillRect(10, 90, (int) ((core.getHealth() / maxCoreHealth) * (PREF_W - 20)) , 15);
         
         if (gameState == PLAYING && countdownNum > 0) {
            g2.setFont(countdownFont);
            FontMetrics cf = g2.getFontMetrics(countdownFont);
            g2.setColor(Color.DARK_GRAY);
            message = countdownNum + "";
            g2.drawString(message, PREF_W / 2 - cf.stringWidth(message) / 2, messageY);
         }
         
         if (gameState == PAUSED) {
            g2.setColor(new Color(200, 200, 200, 150));
            g2.fillRect(0, 0, PREF_W, PREF_H);
            g2.setColor(Color.BLACK);
            message = "PAUSED";
            msgWidth = fm.stringWidth(message);
            messageX = PREF_W / 2 - msgWidth / 2;
            g2.drawString(message, messageX, messageY);
         }
         
      }
   
      if (isGameOver()) {
         super.paintComponent(g); 
//         this.add(restart);
         message = "Game Over!";
         msgWidth = fm.stringWidth(message);
         messageX = PREF_W / 2 - msgWidth / 2;
         g2.drawString(message, messageX, PREF_H / 2 - msgHeight * 3);
         message = "Final Score: " + score;
         msgWidth = fm.stringWidth(message);
         messageX = PREF_W / 2 - msgWidth / 2;
         g2.drawString(message, messageX, PREF_H / 2 - msgHeight * 2);
         
         for (MyButton b : ending) {
            b.drawPiece(g2, this);
         }
      }

   }
   
   private void update() {
      
      if (!isGameOver() && gameState == 1) {
         
         this.removeAll();
         
         if (countdownNum != 0) {
            countdown.update();
            if (countdown.isChanged) countdownNum--;
         }
         else {
            scoreKeeper.update();
            enemyTimer.update();
            for (int i = 0; i < enemies.size(); i++) {
               enemies.get(i).update();
               enemies.get(i).interactUpdate(player, core);
               if (enemies.get(i).getHealth() <= 0) {
                  enemies.remove(i);
               }
            }
            int count = 0;
            if (score % 250 == 0) {
               if (enemyTimer.getDelay() < 1000) {
                  enemyTimer.setDelay(1000);
               }
               else
                  enemyTimer.setDelay(enemyTimer.getDelay() - 100);
            }
            if (enemyTimer.isChanged()) {
               while (count < 5) {
                  addEnemy();
                  count++;
               }
            }
            if (player.isHit() && !player.isInvincible()) {
               player.setHealth(player.getHealth() - 5);
               player.setInvincible(true);
               invincibility.start();
            }
            if (player.isInvincible()) {            
               invincibility.update();
               player.setHit(false);
               if (invincibility.isChanged()) {
                  player.setInvincible(false);
               }
            }
//            for (Platform p : platforms) {
//               p.playerUpdate(player);
//            }
//            player.checkPlatform(platforms);
//            System.out.println(player.isOnPlatform());
            score = (int) scoreKeeper.getElapsed();
            player.update();
         }
      }
      
      repaint();
      
   }
   
//   private void generateLevel() {
//      
//   }
   
   private void showInstructions(Graphics2D g2) {
      
      String[] instructions = {"The nuclear plant you work at had a huge accident,", "turning most things around you into mutants.", "They are out to attack you and destroy the nuclear reactor,", "which will instantly kill you if destroyed.", "Fight off the siege of enemies and save yourself!",
      "Use the left and right arrow keys to move and space bar to shoot.", "To jump onto platforms, press the up arrow keys",
      "If you or the core health run out, you lose."};
      
      for (int i = 0; i < instructions.length; i++) {
         FontMetrics fm = g2.getFontMetrics();
         int msgWidth = fm.stringWidth(instructions[i]);
         int msgHeight = fm.getHeight();
         int messageX = PREF_W / 2 - msgWidth / 2;
         int messageY = PREF_H / 2 - msgHeight / 2 - 100;
         g2.drawString(instructions[i], messageX, messageY + i * 40);
      }
      
   }
   
   private boolean isGameOver() {
      return player.getHealth() == 0 || core.getHealth() == 0;
      
   }
   
   private void reset() {
      
      countdownNum = 3;
      
      timer = new Timer(10, this);
      timer.start();
      
      player = new Player(PREF_W / 2 - 25, GROUND / 2 - 25, 52, 64, 4.5, -25, 1.50, maxPlayerHealth, new Rectangle(PREF_W, GROUND));
      core = new Core(PREF_W / 2 - 50, GROUND - 150, 100, 150, 0, new Rectangle(PREF_W, GROUND), maxCoreHealth);
      enemies = new ArrayList<Enemy>();
      for (int i = 0; i < 5; i++) {
         addEnemy();
      }
      score = 0;
      
      
      try {
         highScore = getHighScore();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      
      
      scoreKeeper = new CountTimer();
      scoreKeeper.setDelay(50);
      enemyTimer = new CountTimer();
      enemyTimer.setDelay(1500);
      invincibility = new CountTimer();
      invincibility.setDelay(1500);
      countdown = new CountTimer();
      countdown.setDelay(1000);
      
   }
   
   private void addEnemy() {
      int assign = ((int) (Math.random() * 2) ) == 0 ? 0 - ((int) (Math.random() * 10) + 40) : PREF_W + (int) (Math.random() * 10);
      enemies.add(new Enemy(assign, GROUND - 75, 75, 75, (Math.random() * 3) + 2, new Rectangle(PREF_W, GROUND), 10));
      Enemy latest = enemies.get(enemies.size() - 1);
      if (latest.getX() >= GROUND) latest.setDx(-latest.getMoveSpeed());
   }
   
   public void saveHighScore() throws IOException {
      
      PrintWriter pw = new PrintWriter(new FileWriter(highScoreFile, false));
      pw.println(score);
      pw.close();
      
   }
   
   public int getHighScore() throws FileNotFoundException {
      
      Scanner reader = new Scanner(new File(highScoreFile));
      int read = 0;
      while(reader.hasNext()){
         read = Integer.parseInt(reader.nextLine());
      }

      reader.close();

      return read;
      
   }
   
   @Override
   public void keyTyped(KeyEvent e) {
   }

   @Override
   public void keyPressed(KeyEvent e) {
      
      if (gameState == PLAYING) {
         if (e.getKeyCode() == KeyEvent.VK_UP && !player.isFalling()) {
            player.setJumping(true);
         }
         if (e.getKeyCode() == KeyEvent.VK_SPACE && countdownNum <= 0) {
            player.fire();
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
      if (e.getKeyCode() == KeyEvent.VK_P && gameState == PLAYING && countdownNum == 0) {
          gameState = PAUSED;
      }
      else if (e.getKeyCode() == KeyEvent.VK_P && gameState == PAUSED) {
         countdownNum = 3;
         countdown.reset();
         gameState = PLAYING;
      }
      
   }

   @Override
   public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
         player.setLeft(false);
      }
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
         player.setRight(false);
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == timer) {
         if (isGameOver()) {
            try {
               if (score > highScore)
                  saveHighScore();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
            timer.stop();
         }
         update();
      }
   } 

   @Override
   public void mousePressed(MouseEvent e) {
   }

   @Override
   public void mouseReleased(MouseEvent e)
   {
      // make the custom buttons work
      
      Point clicked = e.getPoint();
      
      if (back.getSelfBounds().contains(clicked) && gameState == INSTRUCTIONS) {
         gameState = TITLE_SCREEN;
      }
      
      for (MyButton b : utilities) {
         if (b.getSelfBounds().contains(clicked) && gameState == TITLE_SCREEN) {
            if (b == utilities[0]) {
               countdown.reset();
               gameState = PLAYING;
            }
            else if (b == utilities[1]) {
               gameState = INSTRUCTIONS;
            }
            else if (b == utilities[2]) {
               System.exit(0);
            }
//            System.out.println("clicking button");
         }
         
      }
      
      for (MyButton b : ending) {
         if (b.getSelfBounds().contains(clicked) && isGameOver()) {
            if (b == ending[0]) {
               gameState = PLAYING;
               reset();
            }
            else if (b == ending[1]) {
               gameState = TITLE_SCREEN;
               reset();
               repaint();
            }
            System.out.println("clicking button");
         } 
      }
      
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

      JFrame frame = new JFrame("Nuclear Siege");
//      frame.setLayout(null);
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
