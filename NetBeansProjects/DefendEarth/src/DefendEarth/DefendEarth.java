/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DefendEarth;

import GameTools.GameFrame;
import GameTools.Drawable;
import GameTools.GamePanel;
import GravityAPI.API;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 * This game is similar to space invaders but instead of moving left and right
 * the player does not move and but rather changes the angle and power of their
 * shot.
 *
 * @author Nick Villano
 */
public class DefendEarth {

    public static final int FPS = 60;
    private static GameFrame frame;
    public static GamePanel panel;
    private static Controller controller;
    private static JLabel score;
    private static Random rand;

    public static boolean playing = true;
    public static double START_TIME;

    private static BufferedImage EXPLOSION;
    private static BufferedImage GAME_OVER;
    public static final Color GRASS = new Color(70, 137, 102); // green
    public static final Color SKY = new Color(74, 167, 247); // light blue
    private static Font font;

    private static int BASE_LEVEL = 30;

    private static ArrayList<ArrayList<Bullet>> bullets;
    /**
     * The shooter is always at index 0
     */
    private static ArrayList<Drawable> toDraw;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        rand = new Random();

        setFont("PoiretOne-Regular.ttf");
        String explosionFile = "Explosion.png";
        String gameOverFile = "GAME OVER.png";
        try {
            EXPLOSION = ImageIO.read(new File(explosionFile));
            GAME_OVER = ImageIO.read(new File(gameOverFile));
        } catch (IOException e) {
        }

        toDraw = new ArrayList<>();
        bullets = new ArrayList<>();

        panel = new GamePanel(0.5) {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                updateGraphics(g);
            }
        };

        frame = new GameFrame("Defend Earth", panel);
        controller = new Controller();
        frame.addKeyListener(controller);

        score = new JLabel();
        score.setFont(font);
        score.setText("0");
        panel.add(score);

        int shooterXPos = panel.getSize().width / 2;
        int shooterYPos = getBASE_LEVEL();
        toDraw.add(new Shooter(shooterXPos, shooterYPos, 0.0));
        lastPos = new Point(shooterXPos, shooterYPos);

        panel.setBackground(SKY);

        frame.setVisible(true);

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;
        double averageFPS;

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(DefendEarth.class.getName()).log(Level.SEVERE, null, ex);
        }

        START_TIME = System.nanoTime();

        while (true && playing) {
            // used for FPS control
            startTime = System.nanoTime();

            updateGame();
            panel.repaint();

            // used for FPS 
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                Thread.sleep(waitTime);
            } catch (Exception e) {
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                // use these two lines if you want to know the average FPS
                //averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                //System.out.println("Average FPS: " + averageFPS);
                frameCount = 0;
                totalTime = 0;

            }
        }
    }

    private static int lastTime = 0;
    private static int secondsBetweenWaves = 3;
    private static int randomnessWithinWave = 1000; // in pixels
    public static final int MAX_ENEMIES_PER_WAVE = 3;
    public static final int MAX_ENEMIES_IN_GAME = 10;

    public static synchronized void updateGame() {
        checkBulletCollisions();
        checkShooterCollisions();

        // every *secondsBetweenWaves* seconds, do what's inside the if statment one time
        int time = Math.round(Math.round((System.nanoTime() - START_TIME) / 1000000000.0));
        if (time != lastTime && time % secondsBetweenWaves == 0) {
            lastTime = time;
            addEnemies(time);
        }
        for (int i = 0; i < toDraw.size(); i++) {
            if (toDraw.get(i) instanceof Enemy) { // for all the enemies: move and check if they got passed the shooter
                ((Enemy) toDraw.get(i)).move();
                // if the enemy got passed the shooter
                if (((Character) toDraw.get(i)).getPos().y + ((Character) toDraw.get(i)).getSize().height / 2 < 0) {
                    // penalty: remove points
                    ((Shooter) toDraw.get(0)).setPoints(((Shooter) toDraw.get(0)).getPoints() - 1);
                    // remove missed enemy
                    toDraw.remove(i--);
                }
            } else { // for the shooter
                // move around the bottom of the screen
                ((Character) toDraw.get(i)).move();
            }
        }
        for (Drawable d : toDraw) {
            if (d instanceof Enemy) {
                ((Enemy) d).move();
            }
        }

    }

    private static synchronized void addEnemies(int time) {
        int numNewEnemies;

        numNewEnemies = MAX_ENEMIES_PER_WAVE;

        for (int i = 0; i < numNewEnemies; i++) {
            if (toDraw.size() - 1 < MAX_ENEMIES_IN_GAME) {
                AlphaOne newGuy = new AlphaOne(0, 0);

                int x = rand.nextInt(panel.getSize().width - newGuy.getSprite().getWidth()) + newGuy.getSprite().getWidth() / 2;
                int y = rand.nextInt(randomnessWithinWave) + panel.getSize().height + newGuy.getSprite().getHeight() / 2;

                newGuy.setPos(new Point(x, y));
                toDraw.add(newGuy);
            }
        }
    }

    private static Point lastPos;
    private static int lastScore;

    /**
     * Paints all the characters to the JPanel.
     *
     * @param g
     */
    public static synchronized void updateGraphics(Graphics g) {
        drawBullets(g);
        // these three lines are for grass
        for (Drawable d : toDraw) {
            d.drawSelf(g);
        }
        if (toDraw.get(0) instanceof Shooter) {
            // GAME STILL ON
            // display score
            lastScore = ((Shooter) toDraw.get(0)).getPoints();
            score.setText(Integer.toString(lastScore));
        } else {
            // GAME OVER
            // display final score
            score.setText(Integer.toString(lastScore));
            // display shooter explosion
            g.drawImage(EXPLOSION, getLastPos().x - EXPLOSION.getWidth() / 2, panel.getSize().height - (getLastPos().y + EXPLOSION.getHeight() / 2), null);
            // display game over text
            g.drawImage(GAME_OVER, panel.getSize().width / 2 - GAME_OVER.getWidth() / 2, panel.getSize().height / 2 - GAME_OVER.getHeight() / 2, null);
        }

    }

    /**
     * Draws all of the projectiles that have been shot from this shooter and
     * are still in the game.
     *
     * @param g
     */
    public static synchronized void drawBullets(Graphics g) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet.fromAccuratePoint(API.convertPointForJPanel(bullets.get(i).get(0), DefendEarth.panel.getHeight())).drawSelf(g);
            bullets.get(i).remove(0);
            if (bullets.get(i).isEmpty()) {
                bullets.remove(i);
            }
        }
    }

    public static synchronized void addBullet(ArrayList<Bullet> in) {
        bullets.add(in);
    }

    public static synchronized ArrayList<Drawable> getToDraw() {
        return toDraw;
    }

    /**
     * This lets you make changes to the toDraw list. Use the getToDraw() to get
     * the current toDraw list then make needed edits then send the updated list
     * back to the game through this method.
     *
     * @param in The new toDraw list.
     */
    public static synchronized void updateToDraw(ArrayList<Drawable> in) {
        toDraw = in;
    }

    private static synchronized void checkBulletCollisions() {
        for (int i = 0; i < bullets.size(); i++) {

            Bullet bullet = bullets.get(i).get(0);
            boolean killed = false;
            int bulletRadius = bullet.RADIUS;
            int bulletEast = (Math.round(Math.round(bullet.x))) - bulletRadius;
            int bulletWest = (Math.round(Math.round(bullet.x))) + bulletRadius;
            int bulletNorth = (Math.round(Math.round(bullet.y))) - bulletRadius;
            int bulletSouth = (Math.round(Math.round(bullet.y))) + bulletRadius;

            for (int q = 0; q < toDraw.size(); q++) {

                Character character = (Character) toDraw.get(q);

                int targetEast = character.getPos().x + character.getSize().width / 2;
                int targetWest = character.getPos().x - character.getSize().width / 2;
                int targetNorth = character.getPos().y + character.getSize().height / 2;
                int targetSouth = character.getPos().y - character.getSize().height / 2;

                /*
                There may be something wrong with these if statements if the 
                bullet is bigger than the enemy then it probibly wont detect the
                collision unless the corners are part of the collision. Look at 
                the collision checking in checkShooterCollisions(). I think 
                thats more what this code should look like but I couldnt adapt 
                it to work.
                 */
                // eliminates frendly fire
                if (!bullet.ID.equals(character.type())) {
                    // only check if the x coordinates overlap
                    if ((bulletEast < targetEast && bulletWest > targetWest) || (bulletEast < targetWest && bulletWest > targetWest)) {
                        // only check if the y coordinates overlap
                        if ((bulletNorth < targetNorth && bulletSouth > targetSouth) || (bulletNorth < targetSouth && bulletSouth > targetSouth)) {
                            // at this point, their bounding boxes collide
                            // next step is to check if their meshes collide
                            toDraw.remove(q--);
                            killed = true;
                            if (bullet.ID.equals("Shooter")) {
                                ((Shooter) DefendEarth.toDraw.get(0)).addPoints(1);
                            }
                        }
                    }
                }
            }
            if (killed) {
                bullets.remove(i--);
            }
        }
    }

    public static synchronized void checkShooterCollisions() {
        Character shooter = (Character) toDraw.get(0);

        int east = shooter.getPos().x + shooter.getSize().width / 2;
        int west = shooter.getPos().x - shooter.getSize().width / 2;
        int north = shooter.getPos().y + shooter.getSize().height / 2;
        int south = shooter.getPos().y - shooter.getSize().height / 2;

        boolean killed = false;

        for (int q = 1; q < toDraw.size(); q++) {

            Character enemy = (Character) toDraw.get(q);

            int targetEast = enemy.getPos().x + enemy.getSize().width / 2;
            int targetWest = enemy.getPos().x - enemy.getSize().width / 2;
            int targetNorth = enemy.getPos().y + enemy.getSize().height / 2;
            int targetSouth = enemy.getPos().y - enemy.getSize().height / 2;

            /*
            Im not sure why these if statements work but when adapted for 
            collision checking in checkBulletCollisions() it doesnt.
             */
            // eliminates friendly fire
            if (!shooter.type().equals(enemy.type())) {
                //System.out.print("1");
                // only check if the x coordinates overlap
                if (((east < targetEast && east > targetWest) || (west > targetWest && west < targetEast)) || ((east < targetEast && east > targetWest) || (west > targetWest && west < targetEast))) {
                    //System.out.print("2");
                    // only check if the y coordinates overlap
                    if (((north < targetNorth && north > targetSouth) || (south > targetSouth && south < targetNorth)) || ((north < targetNorth && north > targetSouth) || (south > targetSouth && south < targetNorth))) {
                        //System.out.print("3");
                        // at this point, their bounding boxes collide
                        // next step is to check if their meshes collide
                        if (enemy.type().equals("Enemy")) {
                            //System.out.print("!");
                            killed = true;
                            toDraw.remove(q--);
                        }
                    }
                }
            }
        }
        if (killed) {
            toDraw.remove(0);
            playing = false;
        }
    }

    /**
     * @return the BASE_LEVEL
     */
    public static int getBASE_LEVEL() {
        return BASE_LEVEL;
    }

    /**
     * @param aBASE_LEVEL the BASE_LEVEL to set
     */
    public static void setBASE_LEVEL(int aBASE_LEVEL) {
        BASE_LEVEL = aBASE_LEVEL;
    }

    /**
     * @return the lastPos
     */
    public static Point getLastPos() {
        return lastPos;
    }

    /**
     * @param aLastPos the lastPos to set
     */
    public static void setLastPos(Point aLastPos) {
        lastPos = aLastPos;
    }

    public static void setFont(String file) {
        File fontFile = new File(file);
        font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(DefendEarth.class.getName()).log(Level.SEVERE, null, ex);
        }
        font = font.deriveFont(Font.PLAIN, 20);

        GraphicsEnvironment ge
                = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
    }
}
