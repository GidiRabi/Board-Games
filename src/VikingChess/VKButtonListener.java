package VikingChess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Main.MainGUI;

public class VKButtonListener implements ActionListener {
    private MainGUI mainGUI;

    public VKButtonListener(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainGUI.getFrame().setVisible(false);
        Main.startGame();
    }

}