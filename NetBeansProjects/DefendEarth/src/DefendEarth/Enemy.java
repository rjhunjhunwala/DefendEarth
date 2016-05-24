/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DefendEarth;

import GameTools.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Nick Villano
 */
public abstract class Enemy extends Character implements Drawable {

    private BufferedImage sprite;
    private Dimension size;

    public Enemy(int x, int y) {
        setPos(new Point(x, y));
        String fileName = this.toString() + ".png";
        sprite = null;
        try {
            sprite = ImageIO.read(new File(fileName));
        } catch (IOException e) {
        }
        size = new Dimension(sprite.getWidth(), sprite.getHeight());
    }

//    public void move() {
//        int xPos = getPos().x;
//        int yPos = getPos().y;
//
//        int xSpeed = getSpeed().width;
//        int ySpeed = getSpeed().height;
//
//        int xDistance = xPos - ((Character) DefendEarth.getToDraw().get(0)).getPos().x;
//        int yDistance = yPos - ((Character) DefendEarth.getToDraw().get(0)).getPos().y;
//
//        if (xDistance > xSpeed && yDistance <= ySpeed) {
//            
//        } else if (yDistance > ySpeed && xDistance <= xSpeed) {
//            
//        }
//    }
    // ROHAN'S
    public void move() {
        double speed = 1.1;
        int xPos = getPos().x;
        int yPos = getPos().y;
        int xDistance = xPos - ((Character) DefendEarth.getToDraw().get(0)).getPos().x;
        int yDistance = yPos - ((Character) DefendEarth.getToDraw().get(0)).getPos().y;

        //System.out.println(xPos + ", " + yPos);
        double slope;
        if (xDistance != 0) {
            slope = yDistance / xDistance;
        } else {
            slope = -100000;
        }

        double angle = Math.atan(slope);
        double moveX = Math.cos(angle) * speed;
        double moveY = Math.sin(angle) * speed;
        if (xDistance > 0) {
            moveX *= -1;
            moveY *= -1;
        }
        if (yDistance < 0) {
            //System.err.println("stuff");
            moveY *= -1;
            moveX *= 1;
        }
        setPos(new Point((int) (xPos + moveX), (int) (yPos + moveY)));
    }

    @Override
    public String toString() {
        return "Enemy";
    }
}
