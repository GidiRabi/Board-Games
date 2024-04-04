package VikingChess;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import Main.MainGUI;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class Main {
    public static void mainVK() {
        JFrame frame = startGame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        // Use frame here
    }

//    public static JFrame startGame() {
//        PlayableLogic gameLogic = new GameLogic();
//        GUI_for_chess_like_games gui = new GUI_for_chess_like_games(gameLogic, "Vikings Chess Game");
//        SwingUtilities.invokeLater(() -> {
//            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
//            UIManager.put("Button.select", new ColorUIResource(new Color(0, 0, 0, 0)));
//            gui.start();
//        });
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosed(WindowEvent e) {
//                //wait 1 second
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }
//                MainGUI mainGUI = new MainGUI();
//                mainGUI.getFrame().setVisible(true);
//            }
//        });
//        return gui.getFrame();
//    }

    public static JFrame startGame() {
        PlayableLogic gameLogic = new GameLogic();
        GUI_for_chess_like_games gui = new GUI_for_chess_like_games(gameLogic, "Vikings Chess Game");
        JFrame gameFrame = gui.getFrame();
        SwingUtilities.invokeLater(() -> {
            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
            UIManager.put("Button.select", new ColorUIResource(new Color(0, 0, 0, 0)));
            gui.start();
        });
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                //wait 1 second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                MainGUI mainGUI = new MainGUI();
                mainGUI.getFrame().setVisible(true);
            }
        });
        return gameFrame;
    }

}