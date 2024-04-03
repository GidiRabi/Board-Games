package Main;

import javax.swing.*;
import java.awt.*;
import VikingChess.Main;
import Damca.MainD;

public class MainGUI {
    private JFrame frame;

    public MainGUI() {
        frame = new JFrame("Project Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(337, 600);
        frame.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return frame.getSize();
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        frame.setContentPane(contentPane);

        createButtons();

        frame.setVisible(true);
    }

    private void createButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int buttonWidth = frame.getWidth() * 9/10;
        int buttonHeight = frame.getHeight() / 7;

        JButton vikingChessButton = new JButton("Viking Chess") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(buttonWidth, buttonHeight);
            }
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon vikingChessIcon = new ImageIcon("src/Images/viking_chess.png");
                g.drawImage(vikingChessIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        vikingChessButton.setFont(new Font("Arcade Classic", Font.BOLD, 20));
        vikingChessButton.addActionListener(e -> VikingChess.Main.startGame());
        vikingChessButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        frame.getContentPane().add(vikingChessButton);

        frame.getContentPane().add(Box.createVerticalStrut(3));

        JButton damcaButton = new JButton("Damca") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(buttonWidth,buttonHeight);
            }
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon damcaIcon = new ImageIcon("src/Images/damca.png");
                g.drawImage(damcaIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        damcaButton.setFont(new Font("Arial", Font.BOLD, 20));
        damcaButton.addActionListener(e -> MainD.startGame());        damcaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.getContentPane().add(damcaButton);
    }

    public JFrame getFrame() {
        return frame;
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                new MainGUI();
            }
        });

        new MainGUI();
    }
}