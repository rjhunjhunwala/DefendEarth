/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DefendEarth;

import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author Nick Villano
 */
public class AlphaOne extends Enemy {

    public AlphaOne(int x, int y) {
        super(x, y);
        setSpeed(new Dimension(1, 1));
    }

    @Override
    public void move() {
        setPos(new Point(getPos().x, getPos().y - getSpeed().height));
    }

    @Override
    public String toString() {
        return "AlphaOne";
    }

    @Override
    public String type() {
        return "Enemy";
    }
}
