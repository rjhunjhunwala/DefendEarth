/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameTools;

import javax.swing.JFrame;

/**
 *
 * @author Nick Villano
 */
public class GameFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private GamePanel panel;

    public GameFrame(String title, GamePanel inPanel) {
        super(title);
        panel = inPanel;
        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);

    }

}
