//package Damca;
//
//import javax.swing.*;
//import javax.swing.plaf.ColorUIResource;
//import java.awt.*;
//
//public class MainD {
//    /**
//     * The Main function to start the game.
//     * Don't make any changes here
//     * @param args
//     */
//    public static void mainD(String[] args) {
//        PlayableLogic_ gameLogic = new GameLogic_();
//        GUI_for_chess_like_games_ gui = new GUI_for_chess_like_games_(gameLogic, "Damca Game");
//        SwingUtilities.invokeLater(() -> {
//            // Set the UIManager property to make the focus color transparent
//            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
//            UIManager.put("Button.select", new ColorUIResource(new Color(0, 0, 0, 0)));
//            gui.start();
//        });
//    }
//}

package Damca;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class MainD {
    /**
     * The Main function to start the game.
     * Don't make any changes here
     */
    public static void mainD() {
        startGame();
    }

    public static void startGame() {
        PlayableLogic_ gameLogic = new GameLogic_();
        GUI_for_chess_like_games_ gui = new GUI_for_chess_like_games_(gameLogic, "Damca Game");
        SwingUtilities.invokeLater(() -> {
            // Set the UIManager property to make the focus color transparent
            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
            UIManager.put("Button.select", new ColorUIResource(new Color(0, 0, 0, 0)));
            gui.start();
        });
    }
}