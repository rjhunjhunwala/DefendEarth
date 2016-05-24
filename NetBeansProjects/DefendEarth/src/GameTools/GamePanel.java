/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameTools;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Nick Villano
 */
public abstract class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public final double SCALE;

    public GamePanel(double inScale) {
        super();
        SCALE = inScale;
        this.setPreferredSize(new Dimension((int) (1920 * SCALE), (int) (1080 * SCALE)));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
