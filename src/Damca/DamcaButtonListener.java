package Damca;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Main.MainGUI;

public class DamcaButtonListener implements ActionListener {
    private MainGUI mainGUI;

    public DamcaButtonListener(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainGUI.getFrame().setVisible(false);
        MainD.startGame();
    }
}