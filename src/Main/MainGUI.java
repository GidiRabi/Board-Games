//package Main;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import VikingChess.Main;
//import Damca.MainD;
//
//public class MainGUI {
//    private JFrame frame;
//
//    public MainGUI() {
//        frame = new JFrame("Project Selector");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(337, 600);
//        frame.setLocationRelativeTo(null);
//
//        JPanel contentPane = new JPanel() {
//            @Override
//            public Dimension getPreferredSize() {
//                return frame.getSize();
//            }
//
//            @Override
//            public void paintComponent(Graphics g) {
//                super.paintComponent(g);
//            }
//        };
//        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
//
//        frame.setContentPane(contentPane);
//
//        createButtons();
//
//        frame.setVisible(true);
//    }
//
//    private void createButtons() {
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        int buttonWidth = frame.getWidth() * 9/10;
//        int buttonHeight = frame.getHeight() / 7;
//
//        JButton vikingChessButton = new JButton("Viking Chess") {
//            @Override
//            public Dimension getPreferredSize() {
//                return new Dimension(buttonWidth, buttonHeight);
//            }
//            @Override
//            public Dimension getMaximumSize() {
//                return getPreferredSize();
//            }
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//
//                ImageIcon vikingChessIcon = new ImageIcon("src/Images/viking_chess.png");
//                g.drawImage(vikingChessIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
//            }
//        };
//        vikingChessButton.setFont(new Font("Arcade Classic", Font.BOLD, 20));
//
//        vikingChessButton.addActionListener(e -> {
//            frame.setVisible(false); // Hide the menu
//            JFrame gameFrame = VikingChess.Main.startGame();
//            gameFrame.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosed(WindowEvent e) {
//                    SwingUtilities.invokeLater(() -> frame.setVisible(true)); // Show the menu when the game window is closed
//                }
//            });
//        });
//
//
//        vikingChessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        frame.getContentPane().add(vikingChessButton);
//
//        frame.getContentPane().add(Box.createVerticalStrut(3));
//
//        JButton damcaButton = new JButton("Damca") {
//            @Override
//            public Dimension getPreferredSize() {
//                return new Dimension(buttonWidth,buttonHeight);
//            }
//            @Override
//            public Dimension getMaximumSize() {
//                return getPreferredSize();
//            }
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                ImageIcon damcaIcon = new ImageIcon("src/Images/damca.png");
//                g.drawImage(damcaIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
//            }
//        };
//        damcaButton.addActionListener(e -> {
//            frame.setVisible(false); // Hide the menu
//            JFrame gameFrame = MainD.startGame();
//            gameFrame.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosed(WindowEvent e) {
//                    SwingUtilities.invokeLater(() -> frame.setVisible(true)); // Show the menu when the game window is closed
//                }
//            });
//        });
//        damcaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        frame.getContentPane().add(damcaButton);
//    }
//
//    public JFrame getFrame() {
//        return frame;
//    }
//
//    public static void main(String[] args) {
////        Runtime.getRuntime().addShutdownHook(new Thread() {
////            public void run() {
////                new MainGUI();
////            }
////        });
//
//        new MainGUI();
//    }
//}

package Main;

import javax.swing.*;
import java.awt.*;
import Damca.DamcaButtonListener;
import VikingChess.VKButtonListener;

public class MainGUI {
    private JFrame frame;

    public MainGUI() {
        frame = new JFrame("Project Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(337, 600);
        frame.setLocationRelativeTo(null);

        // Create a new JPanel with a background image
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

        // Set the new JPanel as the content pane of the frame
        frame.setContentPane(contentPane);

        createButtons();

        frame.setVisible(true);
    }

    private void createButtons() {
        //make the button centered width
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int buttonWidth = frame.getWidth() * 9/10;
        int buttonHeight = frame.getHeight() / 7;

        JButton pacmanButton = new JButton("Damca") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(buttonWidth, buttonHeight);
            }
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
            //add image to button background
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon pacmanIcon = new ImageIcon("src/Images/pacman.png");
                g.drawImage(pacmanIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        pacmanButton.setFont(new Font("Arcade Classic", Font.BOLD, 20));
        pacmanButton.addActionListener(new DamcaButtonListener(this));
        pacmanButton.setAlignmentX(Component.CENTER_ALIGNMENT); // This centers the button along the x-axis

        frame.getContentPane().add(pacmanButton);

        // Add a vertical strut for spacing
        frame.getContentPane().add(Box.createVerticalStrut(3));

        JButton calculatorButton = new JButton("Viking Chess") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(buttonWidth,buttonHeight);
            }
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
            //add image to button background
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon calculatorIcon = new ImageIcon("src/Images/calc.jpeg");
                g.drawImage(calculatorIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        calculatorButton.setFont(new Font("Arial", Font.BOLD, 20));
        calculatorButton.addActionListener(new VKButtonListener(this));
        calculatorButton.setAlignmentX(Component.CENTER_ALIGNMENT); // This centers the button along the x-axis

        frame.getContentPane().add(calculatorButton);
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