/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DefendEarth;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Nick Villano
 */
public class Controller implements KeyListener {

    private boolean canShoot = true;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'A':
                if (DefendEarth.getToDraw().get(0) instanceof Shooter) {
                    ((Shooter) DefendEarth.getToDraw().get(0)).rotatePort(true);
                }
                break;
            case 'D':
                if (DefendEarth.getToDraw().get(0) instanceof Shooter) {
                    ((Shooter) DefendEarth.getToDraw().get(0)).rotateStarboard(true);
                }
                break;
            case 'a':
                if (DefendEarth.getToDraw().get(0) instanceof Shooter) {
                    ((Shooter) DefendEarth.getToDraw().get(0)).rotatePort(false);
                }
                break;
            case 'd':
                if (DefendEarth.getToDraw().get(0) instanceof Shooter) {
                    ((Shooter) DefendEarth.getToDraw().get(0)).rotateStarboard(false);
                }
                break;
            case ' ':
                if (canShoot) {
                    if (DefendEarth.getToDraw().get(0) instanceof Shooter) {
                        ((Shooter) DefendEarth.getToDraw().get(0)).shoot();
                    }
                    canShoot = false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        canShoot = true;
    }

}
