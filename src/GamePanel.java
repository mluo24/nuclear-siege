import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

// I stole this from your website
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
   
   private static final long serialVersionUID = 1L;
   
   // constants
   private static final int PREF_W = 1200;
   private static final int PREF_H = 600;
   private static final int GROUND = PREF_H - 90;
   
   // panel utilities
//   private RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
   private Font font = new Font("ArcadeClassic", Font.PLAIN, 40);
   private Font countdownFont = new Font("Verdana", Font.PLAIN, 250);
   private String highScoreFile = "src/highscore.txt";
   private JButton[] utilities = {new JButton("Play"), new JButton("Exit")};
   private JButton restart = new JButton("Reset");
   
   // game elements
   private int gameState = 0;
   private Timer timer;
   private Player player;
   private Core core;
   private ArrayList<Enemy> enemies;
   private ArrayList<Platform> obstacles;
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
      
      this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      this.setAlignmentX(CENTER_ALIGNMENT);
      
      for (JButton b : utilities) {
         b.setFont(font);
         b.setPreferredSize(new Dimension(100, 40));
         b.setAlignmentX(JButton.CENTER_ALIGNMENT);
         b.addActionListener(this);
         this.add(b);
      }
      
      restart.setPreferredSize(new Dimension(100, 40));
      restart.setAlignmentX(JButton.CENTER_ALIGNMENT);
      restart.addActionListener(this);
      
            
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
      g2.setColor(Color.BLACK);
      
      //PAINT TO THE PANEL HERE
      if (gameState == 0) {
         
         g2.drawString("GAME TITLE", PREF_W / 2 - 100, PREF_H / 2 - 20);
         
      }

      // top UI
      if (gameState == 1 || gameState == 2) {
         super.paintComponent(g);
         g2.drawString("Score: " + score, 10, 40);
         g2.drawString("High Score: " + highScore, PREF_W - 325, 40);
         g2.drawString("Core health:", 10, 80);
//         g2.drawString("Player health: " + player.getHealth(), 220, 200);
         
         // draw ground
         g2.setColor(Color.BLACK);
         g2.fillRect(0, GROUND, PREF_W, PREF_H - GROUND);
         
         // draw core
         g2.setColor(Color.BLUE);
         core.drawPiece(g2);
         
         // draw enemies
         g2.setColor(Color.YELLOW);
         for (Enemy enemy : enemies) {
            enemy.drawPiece(g2);
         }
         
         // draw player
         if (player.isInvincible()) {
            g2.setColor(Color.CYAN);
   //         g2.drawString("poof", (int) player.getX(), (int) player.getY());
         }
         else {
            g2.setColor(Color.GREEN);
         }
         player.drawPiece(g2);
         
         // draw health bars
         g2.setColor(Color.RED);
         // player
         g2.drawRect((int) player.getX(), (int) player.getY() - 20, (int) player.getWidth(), 10);
         g2.fillRect((int) player.getX(), (int) player.getY() - 20, (int) ((player.getHealth() / maxPlayerHealth) * (player.getWidth())) , 10);
         // core
         g2.drawRect(10, 90, PREF_W - 20, 20);
         g2.fillRect(10, 90, (int) ((core.getHealth() / maxCoreHealth) * (PREF_W - 20)) , 20);
         
         if (gameState == 1 && countdownNum > 0) {
            g2.setFont(countdownFont);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(countdownNum + "", PREF_W / 2 - 50, PREF_H / 2 + 50);
         }
         
         if (gameState == 2) {
            g2.setColor(new Color(200, 200, 200, 150));
            g2.fillRect(0, 0, PREF_W, PREF_H);
            g2.setColor(Color.BLACK);
            g2.drawString("PAUSED", PREF_W / 2 - 100, PREF_H / 2 - 20);
         }
         
      }
   
      if (isGameOver()) {
         super.paintComponent(g); 
//         this.add(restart);
         g2.drawString("Game Over!", PREF_W / 2 - 100, PREF_H / 2 - 20);
         g2.drawString("Final Score: " + score, PREF_W / 2 - 150, PREF_H / 2 + 20);
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
            if (score % 100 == 0) {
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
            score = (int) scoreKeeper.getElapsed();
            player.update();
         }
      }
      repaint();
      
   }
   
   private boolean isGameOver() {
      try {
         if (score > highScore)
            saveHighScore();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return player.getHealth() == 0 || core.getHealth() == 0;
   }
   
   private void reset() {
      
      countdownNum = 3;
      
      timer = new Timer(10, this);
      timer.start();
      
      player = new Player(PREF_W / 2 - 25, GROUND / 2 - 25, 50, 50, 10, -30, 1.75, maxPlayerHealth, new Rectangle(PREF_W, GROUND));
      core = new Core(PREF_W / 2 - 50, GROUND - 150, 100, 150, 0, new Rectangle(PREF_W, GROUND), maxCoreHealth);
      enemies = new ArrayList<Enemy>();
      for (int i = 0; i < 5; i++) {
         addEnemy();
      }
      obstacles = new ArrayList<Platform>();
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
      enemies.add(new Enemy(assign, GROUND - 75, 40, 75, (Math.random() * 3) + 2, new Rectangle(PREF_W, GROUND), 10));
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
      if (gameState == 1) {
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
      if (e.getKeyCode() == KeyEvent.VK_P && gameState == 1 && countdownNum == 0) {
          gameState = 2;
      }
      else if (e.getKeyCode() == KeyEvent.VK_P && gameState == 2) {
         countdownNum = 3;
         countdown.reset();
         gameState = 1;
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
      if (e.getSource() == utilities[0]) {
         countdown.reset();
         gameState = 1;
      }
      else if (e.getSource() == utilities[1]) {
         System.exit(0);
      }
      else if (e.getSource() == restart) {
         gameState = 1;
         reset();
         this.removeAll();
      }
//      if (e.getSource() instanceof Timer)
//         System.out.println("okay");
      else if (e.getSource() == timer) {
         if (isGameOver()) {
//            gameState = 2;
            this.add(restart);
            this.revalidate();
            timer.stop();
         }
         update();
      }
   } 

   @Override
   public void mousePressed(MouseEvent e) {
//      System.out.println("PRESSING");
//      for (JButton b : utilities) {
//         b.setPreferredSize(new Dimension(100, 40));
//         b.setAlignmentX(JButton.CENTER_ALIGNMENT);
//         b.addActionListener(this);
//         this.add(b);
//      }
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
