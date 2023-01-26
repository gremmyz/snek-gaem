package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int screenWidth = 1300;
    static final int screenHeight = 750;
    static final int objectSize = 25;
    static final int totalObjects = (screenWidth*screenHeight)/(objectSize*objectSize);
    static final int timeDelay = 50;
    final int[] xSnake = new int[totalObjects];
    final int[] ySnake = new int[totalObjects];
    int snakeSize = 6;
    int applesEaten;
    int xCoordinateOfApple;
    int yCoordinateOfApple;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    // Start game
    public void startGame() {
        spawnApple();
        running = true;
        timer = new Timer(timeDelay,this);
        timer.start();
    }

    // Create UI
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }
    public void draw(Graphics graphics) {
        if(running) {
            graphics.setColor(Color.red);
            graphics.fillOval(xCoordinateOfApple, yCoordinateOfApple, objectSize, objectSize);

            for(int i = 0; i< snakeSize;i++) {
                if(i == 0) {
                    graphics.setColor(Color.green);
                    graphics.fillRect(xSnake[i], ySnake[i], objectSize, objectSize);
                }
                else {
                    graphics.setColor(new Color(45,180,0));
                    graphics.fillRect(xSnake[i], ySnake[i], objectSize, objectSize);
                }
            }
            graphics.setColor(Color.white);
            graphics.setFont( new Font("Comic Sans MS",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: "+applesEaten, (screenWidth - metrics.stringWidth("Score: "+applesEaten))/2, graphics.getFont().getSize());
        }
        else {
            gameOver(graphics);
        }
    }

    // snake.Snake move method
    public void move(){
        for(int i = snakeSize;i>0;i--) {
            xSnake[i] = xSnake[i-1];
            ySnake[i] = ySnake[i-1];
        }

        switch(direction) {
            case 'U':
                ySnake[0] = ySnake[0] - objectSize;
                break;
            case 'D':
                ySnake[0] = ySnake[0] + objectSize;
                break;
            case 'L':
                xSnake[0] = xSnake[0] - objectSize;
                break;
            case 'R':
                xSnake[0] = xSnake[0] + objectSize;
                break;
        }
    }

    // Create apple
    public void spawnApple(){
        xCoordinateOfApple = random.nextInt(screenWidth/objectSize)*objectSize;
        yCoordinateOfApple = random.nextInt(screenHeight/objectSize)*objectSize;
    }

    // Apple eat check
    public void eatenApple() {
        if((xSnake[0] == xCoordinateOfApple) && (ySnake[0] == yCoordinateOfApple)) {
            snakeSize++;
            applesEaten++;
            spawnApple();
        }
    }

    // Collision Check
    public void checkCollisions() {
        // Check collision self
        for(int i = snakeSize;i>0;i--) {
            if ((xSnake[0] == xSnake[i]) && (ySnake[0] == ySnake[i])) {
                running = false;
                break;
            }
        }
        // Check collision left
        if(xSnake[0] < 0) {
            running = false;
        }
        // Check collision right
        if(xSnake[0] > screenWidth) {
            running = false;
        }
        // Check collision top
        if(ySnake[0] < 0) {
            running = false;
        }
        // Check collision bottom
        if(ySnake[0] > screenHeight) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    // Create game over screen
    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.setFont( new Font("Comic Sans MS",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: "+applesEaten, (screenWidth - metrics1.stringWidth("Score: "+applesEaten))/2, graphics.getFont().getSize());

        graphics.setColor(Color.red);
        graphics.setFont( new Font("Comic Sans MS",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over!", (screenWidth - metrics2.stringWidth("Game Over"))/2, screenHeight/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            eatenApple();
            checkCollisions();
        }
        repaint();
    }

    // Takes key input
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case (KeyEvent.VK_A):
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case (KeyEvent.VK_D):
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case (KeyEvent.VK_W):
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case (KeyEvent.VK_S):
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
