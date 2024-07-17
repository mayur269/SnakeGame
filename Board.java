
package snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener{
    private Image apple;
    private Image dot;
    private Image head;
    private int dots;
    private int score;  // Score tracking
    
    private Timer timer;
    
    
    
    private final int ALL_DOTS = 6400;
    private final int DOT_SIZE = 10;
    
    private final int RANDOM_POSITION = 29;
    
    private int apple_x;
    private int apple_y;
    
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS]; 
    
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    
    private boolean inGame = true;
    
    
    
    
    Board(){
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
//        setBackground(new Color(34, 34, 34)); // Change background color to dark gray
        setPreferredSize(new Dimension(800,800));
        setFocusable(true);
        
        loadImages();
        initGame();
        
    }
    
    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
        apple = i1.getImage();
        ImageIcon i2= new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
        dot = i2.getImage();
        ImageIcon i3= new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head2.jpg"));
        head = i3.getImage().getScaledInstance(10, 20, Image.SCALE_SMOOTH);
    }
    
    public void initGame() {
        dots = 3;
        score = 0;  // Initialize score
        for(int i=0; i<dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }
        
        locateApple();
        timer = new Timer(150, this);
        timer.start();
    }
    
     public void locateApple() {
        int r = (int )(Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE; 
        
        r = (int )(Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            Draw(g);
        } else {
            gameOver(g);
        }
    }
    
    public void Draw(Graphics g) {
        if(inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
    
            for(int i=0; i<dots; i++) {
                if(i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            drawScore(g);  // Draw score
            Toolkit.getDefaultToolkit().sync();;
        } else {
            gameOver(g);
        }
    }
    
    public void drawScore(Graphics g) {
        String scoreMsg = "Score: " + score;
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(scoreMsg, 10, 10);  // Display score at top left corner
    }
    
    public void gameOver(Graphics g) {
        String msg = "Game Over!";
        String scoreMsg = "Final Score: " + score;  // Final score on game over
        Font font = new Font("SERIF",Font.BOLD,14);
        g.setColor(Color.WHITE);
        g.setFont(font);
        FontMetrics metrics = getFontMetrics(font);
        g.drawString(msg, (800 - metrics.stringWidth(msg))/2, 800/2);
    }
   
    public void move() {
        for(int i=dots; i>0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }
    
    public void checkApple() {
        if((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;  // Increase score
            locateApple();
        }
    }
   
    public void checkCollision() {
        // Check for collision with itself
        for(int i = dots; i > 0; i--) {
            if((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }
         // Check for collision with boundaries
        if(y[0] >= getHeight()) {
            inGame = false;
        }
        if(x[0] >= getHeight()) {
            inGame = false;
        }
        if(y[0] < 0) {
            inGame = false;
        }
        if(x[0] < 0) {
            inGame = false;
        }
        if(!inGame) {
            timer.stop();
        }
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(inGame) {
            checkApple();
            checkCollision();
            move();
        }
       
        
        repaint();
    }
    public class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            
            if(key == KeyEvent.VK_LEFT && (!rightDirection)){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_UP && (!downDirection)){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && (!upDirection)){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
   
}
