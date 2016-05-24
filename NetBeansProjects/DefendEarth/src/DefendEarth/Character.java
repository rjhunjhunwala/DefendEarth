/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DefendEarth;

import GameTools.Drawable;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Nick Villano
 */
public abstract class Character implements Drawable {

    private BufferedImage sprite;
    private Point pos;
    private Dimension size;
    private double heading;
    private Dimension speed;

    public Character() {
        String fileName = this.toString() + ".png";
        setSprite(null);
        try {
            setSprite(ImageIO.read(new File(fileName)));
        } catch (IOException e) {

        }
        setSize(new Dimension(getSprite().getWidth(), getSprite().getHeight()));
    }

    /**
     * @return the pos
     */
    public Point getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(Point pos) {
        this.pos = pos;
    }

    /**
     * @return the heading
     */
    public double getHeading() {
        return heading % 360;
    }

    /**
     * Sets the heading while keeping the value positive and within 360 degrees.
     *
     * @param inHeading the heading to set
     */
    public void setHeading(double inHeading) {
        heading = (360 + inHeading % 360) % 360;
    }

    /**
     * @return the sprite
     */
    public final BufferedImage getSprite() {
        return sprite;
    }

    /**
     * @param sprite the sprite to set
     */
    public final void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    /**
     * @return the size
     */
    public final Dimension getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public final void setSize(Dimension size) {
        this.size = size;
    }

    /**
     * Moves north with out of bounds checking.
     */
    public void moveNorth() {
        if (getSpeed().height > 0) {
            if (getPos().y - getSpeed().height - getSize().height / 2 >= 0) {
                setPos(new Point(getPos().x, getPos().y - getSpeed().height));
            } else {
                speed.height -= 1;
                moveNorth();
                speed.height += 1;
            }
        }
    }

    /**
     * Moves south with out of bounds checking.
     */
    public void moveSouth() {
        if (getSpeed().height > 0) {
            if (getPos().y + getSpeed().height + getSize().height / 2 <= DefendEarth.panel.getHeight()) {
                setPos(new Point(getPos().x, getPos().y + getSpeed().height));
            } else {
                speed.height -= 1;
                moveSouth();
                speed.height += 1;
            }
        }
    }

    /**
     * Moves east with out of bounds checking.
     */
    public void moveEast() {
        if (getSpeed().width > 0) {
            if (getPos().x + getSpeed().width + getSize().width / 2 <= DefendEarth.panel.getWidth()) {
                setPos(new Point(getPos().x + getSpeed().width, getPos().y));
            } else {
                speed.width -= 1;
                moveEast();
                speed.width += 1;
            }
        }
    }

    /**
     * Moves west with out of bounds checking.
     */
    public void moveWest() {
        if (getSpeed().width > 0) {
            if (getPos().x - getSpeed().width - getSize().width / 2 >= 0) {
                setPos(new Point(getPos().x - getSpeed().width, getPos().y));
            } else {
                speed.width -= 1;
                moveWest();
                speed.width += 1;
            }
        }
    }

    /**
     * @return the speed
     */
    public Dimension getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public final void setSpeed(Dimension speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Character";
    }

    public abstract String type();

    @Override
    public void drawSelf(Graphics g) {
        g.drawImage(getSprite(), getPos().x - getSize().width / 2, DefendEarth.panel.getHeight() - getPos().y - getSize().height / 2, null);
    }

    public abstract void move();
}
