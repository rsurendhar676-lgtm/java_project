package com.snakegame;

import javax.swing.*;
import java.awt.*;

/**
 * Main application frame for the Snake game.
 */
public class SnakeGame extends JFrame {
    private final GamePanel gamePanel;

    public SnakeGame() {
        setTitle("Advanced Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0x1a1a2e));

        gamePanel = new GamePanel(this);
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
    }

    public void showGameSetup() {
        GameSetupDialog dialog = new GameSetupDialog(this, gamePanel);
        dialog.setVisible(true);
    }

    public void showGameOver(int score, int highScore) {
        GameOverDialog dialog = new GameOverDialog(this, gamePanel, score, highScore);
        dialog.setVisible(true);
    }

    public void startGameWithSettings(Difficulty difficulty, boolean wallMode) {
        gamePanel.setDifficulty(difficulty);
        gamePanel.setWallMode(wallMode);
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
