package com.snakegame;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog shown when the game ends.
 */
public class GameOverDialog extends JDialog {
    public GameOverDialog(SnakeGame parentFrame, GamePanel gamePanel, int score, int highScore) {
        super(parentFrame, "Game Over", true);

        setLayout(new BorderLayout(15, 15));
        setSize(340, 280);
        setLocationRelativeTo(parentFrame);
        getContentPane().setBackground(new Color(0x1a1a2e));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(new Color(0x1a1a2e));
        center.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Game Over");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0xe74c3c));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(titleLabel);
        center.add(Box.createVerticalStrut(15));

        JLabel scoreLabel = new JLabel("Your Score: " + score);
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(scoreLabel);
        center.add(Box.createVerticalStrut(5));

        JLabel highLabel = new JLabel("High Score: " + highScore);
        highLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        highLabel.setForeground(new Color(0x4ecca3));
        highLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(highLabel);
        center.add(Box.createVerticalStrut(25));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(new Color(0x1a1a2e));
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        JTextField nameField = new JTextField(15);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        center.add(namePanel);
        center.add(Box.createVerticalStrut(15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(new Color(0x1a1a2e));

        JButton saveBtn = new JButton("Save Score & Play Again");
        saveBtn.setBackground(new Color(0x4ecca3));
        saveBtn.setForeground(Color.BLACK);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) name = "Player";
            gamePanel.getHighScoreManager().addScore(name, score, gamePanel.getDifficulty());
            dispose();
            parentFrame.showGameSetup();
        });

        JButton playAgainBtn = new JButton("Play Again");
        playAgainBtn.setBackground(new Color(0x45b393));
        playAgainBtn.setForeground(Color.WHITE);
        playAgainBtn.setFocusPainted(false);
        playAgainBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                gamePanel.getHighScoreManager().addScore(name, score, gamePanel.getDifficulty());
            }
            dispose();
            parentFrame.showGameSetup();
        });

        JButton menuBtn = new JButton("Menu");
        menuBtn.setFocusPainted(false);
        menuBtn.addActionListener(e -> {
            gamePanel.resetToMenu();
            dispose();
        });

        btnPanel.add(saveBtn);
        btnPanel.add(playAgainBtn);
        btnPanel.add(menuBtn);
        center.add(btnPanel);

        add(center, BorderLayout.CENTER);
    }
}
