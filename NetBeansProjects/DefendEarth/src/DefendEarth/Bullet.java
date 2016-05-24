/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DefendEarth;

import GameTools.Drawable;
import GravityAPI.AccuratePoint;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Nick Villano
 */
public class Bullet extends AccuratePoint implements Drawable {

    public static final int DEFAULT_RADIUS = 4;
    public final int RADIUS;
    public final String ID;

    public static ArrayList<Bullet> fromAccuratePointPath(ArrayList<AccuratePoint> in) {
        ArrayList<Bullet> out = new ArrayList<>();
        for (AccuratePoint a : in) {
            out.add(new Bullet(a.x, a.y));
        }
        return out;
    }

    public static Bullet fromAccuratePoint(AccuratePoint io) {
        return new Bullet(io.x, io.y);
    }

    public static ArrayList<Bullet> fromAccuratePointPath(ArrayList<AccuratePoint> in, String inID) {
        ArrayList<Bullet> out = new ArrayList<>();
        for (AccuratePoint a : in) {
            out.add(new Bullet(a.x, a.y, inID));
        }
        return out;
    }

    public static Bullet fromAccuratePoint(AccuratePoint io, String inID) {
        return new Bullet(io.x, io.y, inID);
    }

    public Bullet(double inX, double inY) {
        super(inX, inY);
        RADIUS = DEFAULT_RADIUS;
        ID = null;
    }

    public Bullet(double inX, double inY, String inID) {
        super(inX, inY);
        RADIUS = DEFAULT_RADIUS;
        ID = inID;
    }

    public Bullet(double inX, double inY, int inSize, String inID) {
        super(inX, inY);
        RADIUS = inSize;
        ID = inID;
    }

    @Override
    public void drawSelf(Graphics g) {
        g.fillOval(Math.round(Math.round(x - RADIUS)), Math.round(Math.round(y - RADIUS)), 2 * RADIUS, 2 * RADIUS);
    }
}
