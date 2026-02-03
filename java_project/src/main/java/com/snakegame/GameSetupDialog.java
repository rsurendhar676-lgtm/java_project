package com.snakegame;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for configuring game options before starting.
 */
public class GameSetupDialog extends JDialog {
    private final JComboBox<Difficulty> difficultyCombo;
    private final JCheckBox wallModeCheck;
    private final SnakeGame parentFrame;
    private final GamePanel gamePanel;

    public GameSetupDialog(SnakeGame parentFrame, GamePanel gamePanel) {
        super(parentFrame, "Game Setup", true);
        this.parentFrame = parentFrame;
        this.gamePanel = gamePanel;

        setLayout(new BorderLayout(15, 15));
        setSize(320, 220);
        setLocationRelativeTo(parentFrame);
        getContentPane().setBackground(new Color(0x1a1a2e));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(new Color(0x1a1a2e));
        center.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(diffLabel);
        center.add(Box.createVerticalStrut(5));

        difficultyCombo = new JComboBox<>(Difficulty.values());
        difficultyCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        difficultyCombo.setMaximumSize(new Dimension(250, 30));
        center.add(difficultyCombo);
        center.add(Box.createVerticalStrut(15));

        wallModeCheck = new JCheckBox("Wall mode (die on boundary)", true);
        wallModeCheck.setForeground(Color.WHITE);
        wallModeCheck.setBackground(new Color(0x1a1a2e));
        wallModeCheck.setOpaque(true);
        wallModeCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(wallModeCheck);
        center.add(Box.createVerticalStrut(20));

        JButton startBtn = new JButton("Start Game");
        startBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        startBtn.setBackground(new Color(0x4ecca3));
        startBtn.setForeground(Color.BLACK);
        startBtn.setFocusPainted(false);
        startBtn.addActionListener(e -> {
            parentFrame.startGameWithSettings(
                    (Difficulty) difficultyCombo.getSelectedItem(),
                    wallModeCheck.isSelected());
            dispose();
        });

        center.add(startBtn);
        add(center, BorderLayout.CENTER);
    }
}
