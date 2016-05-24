/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DefendEarth;

import GameTools.*;
import GravityAPI.AccuratePoint;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * The main character of the game.
 *
 * @author Nick Villano
 */
public class Shooter extends Character implements Drawable {

    public final int turningSpeed = 15;
    public final int scopingAccuracy = 3;
    public final double maxPower = 140;
    private int points;

    /**
     * Creates a new shooter.
     *
     * @param x The x location that the shooter will spawn at.
     * @param y The y location that the shooter will spawn at.
     * @param inHeading The compass heading that the shooter is facing. The
     * direction's work like reference angles on a unit circle but 0 degrees is
     * facing north (up). This can be positive or negative and can be above 360
     * degrees.
     */
    public Shooter(int x, int y, double inHeading) {
        super.setPos(new Point(x, y));
        super.setHeading(inHeading);
        points = 0;
        setSpeed(new Dimension(2, 2));
    }

    /**
     * Rotates the shooter in the port direction.
     *
     * @param scoping If true, the heading will change by a lower amount.
     */
    public void rotatePort(boolean scoping) {
        if (((getHeading() + (scoping ? turningSpeed / scopingAccuracy : turningSpeed)) % 360) <= 90
                || ((getHeading() + (scoping ? turningSpeed / scopingAccuracy : turningSpeed)) % 360) >= 270) {
            setHeading((getHeading() + (scoping ? turningSpeed / scopingAccuracy : turningSpeed)) % 360);
        } else {
            setHeading(90);
        }
    }

    /**
     * Rotates the shooter in the starboard direction.
     *
     * @param scoping If true, the heading will change by a lower amount.
     */
    public void rotateStarboard(boolean scoping) {
        if (((getHeading() - (scoping ? turningSpeed / scopingAccuracy : turningSpeed)) % 360) <= 90
                || ((getHeading() - (scoping ? turningSpeed / scopingAccuracy : turningSpeed)) % 360) >= 270) {
            setHeading((getHeading() - (scoping ? turningSpeed / scopingAccuracy : turningSpeed)) % 360);
        } else {
            setHeading(270);
        }
    }

    public void shoot() {
        ArrayList<Bullet> bullet = Bullet.fromAccuratePointPath(GravityAPI.API.projectileSimulator(new AccuratePoint(getPos().x, getPos().y), getHeading() + 90, maxPower, Math.round(Math.round(DefendEarth.FPS / 4)), 0, 0, DefendEarth.panel.getWidth(), DefendEarth.panel.getHeight()), type());
        DefendEarth.addBullet(bullet);
    }

    /**
     * Draws the sprite (the name of the class + ".gif" stored in the main
     * folder of the project) with the center of the sprite at the position of
     * the character. Also draws a sight and any bullets that the shooter has
     * shot and are still in the game.
     *
     * @param g
     */
    @Override
    public void drawSelf(Graphics g) {
        drawSight(g);
        super.drawSelf(g);
    }

    /**
     * Draws a line segment that serves as a sight for the player to shoot more
     * accurate
     *
     * @param g
     */
    public void drawSight(Graphics g) {
        Point end = calcSight();
        g.setColor(java.awt.Color.GRAY);
        g.drawLine(getPos().x, DefendEarth.panel.getHeight() - getPos().y, end.x, end.y);
        g.setColor(java.awt.Color.BLACK);
    }

    /**
     * Calculates the end point for the line segment using unit circle trig and
     * multiplying the length of the lines by 64.
     *
     * @return The end point for the sight.
     */
    public Point calcSight() {
        double refAngle = getHeading() - 90;
        double x = Math.cos(Math.toRadians(refAngle)) * 40;
        double y = Math.sin(Math.toRadians(refAngle)) * 40;
        return new Point(Math.round(Math.round((getPos().x - x))), Math.round(Math.round((y - getPos().y) + DefendEarth.panel.getHeight())));
    }

    /**
     * Returns the String value of the object.
     *
     * @return "Shooter"
     */
    @Override
    public String toString() {
        return "Shooter";
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Adds points to the current number of points.
     *
     * @param morePoints The number of points to add.
     */
    public void addPoints(int morePoints) {
        this.setPoints(this.getPoints() + morePoints);
    }

    @Override
    public String type() {
        return "Shooter";
    }
    /**
     * True if the shooter is moving east. If false, the shoote is moving west
     * instead. Used in move().
     */
    public boolean movingEast = true;

    @Override
    public void move() {
        if (movingEast) {
            if (getPos().x + getSpeed().width + getSize().width / 2 < DefendEarth.panel.getSize().width) {
                moveEast();
            } else {
                moveWest();
                movingEast = false;
            }
        } else if (getPos().x - getSpeed().width - getSize().width / 2 > 0) {
            moveWest();
        } else {
            moveEast();
            movingEast = true;
        }
        DefendEarth.setLastPos(getPos());
    }
}
