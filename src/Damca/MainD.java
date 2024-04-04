package Damca;

import Main.MainGUI;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class MainD {
    public static void mainD() {
        startGame();
    }

    public static void startGame() {
        PlayableLogic_ gameLogic = new GameLogic_();
        GUI_for_chess_like_games_ gui = GUI_for_chess_like_games_.getInstance(gameLogic, "Damca Game");
        SwingUtilities.invokeLater(() -> {
            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
            UIManager.put("Button.select", new ColorUIResource(new Color(0, 0, 0, 0)));
            gui.start();
        });
    }
}