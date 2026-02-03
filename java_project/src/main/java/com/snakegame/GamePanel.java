package com.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main game panel handling rendering, input, and game logic.
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 20;
    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 25;
    private static final Color BG_COLOR = new Color(0x1a1a2e);
    private static final Color GRID_COLOR = new Color(0x16213e);
    private static final Color SNAKE_HEAD = new Color(0x4ecca3);
    private static final Color SNAKE_BODY = new Color(0x45b393);

    private Snake snake;
    private Food food;
    private List<Obstacle> obstacles;
    private GameState state = GameState.MENU;
    private Difficulty difficulty = Difficulty.MEDIUM;
    private int score;
    private int currentDelay;
    private long speedBoostUntil;
    private long doublePointsUntil;
    private long invincibilityUntil;
    private boolean wallMode = true;
    private final HighScoreManager highScores;
    private Timer timer;
    private final SnakeGame parentFrame;

    public GamePanel(SnakeGame parentFrame) {
        this.parentFrame = parentFrame;
        this.highScores = new HighScoreManager();
        setPreferredSize(new Dimension(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE));
        setBackground(BG_COLOR);
        setFocusable(true);
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
    }

    public void startGame() {
        int startX = GRID_WIDTH / 2;
        int startY = GRID_HEIGHT / 2;
        snake = new Snake(startX, startY);
        List<Point> emptyObstacles = new ArrayList<>();
        food = Food.createRandom(GRID_WIDTH, GRID_HEIGHT, snake, emptyObstacles, 0);
        obstacles = Obstacle.generateObstacles(difficulty.getObstacleCount(),
                GRID_WIDTH, GRID_HEIGHT, snake, food.getPosition());
        score = 0;
        speedBoostUntil = 0;
        doublePointsUntil = 0;
        invincibilityUntil = 0;
        currentDelay = difficulty.getDelayMs();
        state = GameState.PLAYING;
        startTimer();
        requestFocusInWindow();
        repaint();
    }

    private void startTimer() {
        if (timer != null) timer.stop();
        timer = new Timer(currentDelay, this);
        timer.start();
    }

    private int getEffectiveDelay() {
        int delay = currentDelay;
        if (System.currentTimeMillis() < speedBoostUntil) {
            delay = Math.max(30, delay / 2);
        }
        return delay;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state != GameState.PLAYING) return;

        int effectiveDelay = getEffectiveDelay();
        if (timer.getDelay() != effectiveDelay) {
            timer.setDelay(effectiveDelay);
        }

        snake.move();

        if (checkWallCollision() || checkObstacleCollision() || checkSelfCollision()) {
            gameOver();
            return;
        }

        Point head = snake.getHead();
        if (head.equals(food.getPosition())) {
            eatFood();
        }

        repaint();
    }

    private boolean checkWallCollision() {
        if (!wallMode) {
            snake.wrapHead(GRID_WIDTH, GRID_HEIGHT);
            return false;
        }
        if (System.currentTimeMillis() < invincibilityUntil) return false;
        Point head = snake.getHead();
        return head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT;
    }

    private boolean checkObstacleCollision() {
        if (System.currentTimeMillis() < invincibilityUntil) return false;
        Point head = snake.getHead();
        for (Obstacle obs : obstacles) {
            if (obs.getPositions().contains(head)) return true;
        }
        return false;
    }

    private boolean checkSelfCollision() {
        if (System.currentTimeMillis() < invincibilityUntil) return false;
        return snake.collidesWithSelf();
    }

    private void eatFood() {
        PowerUpType type = food.getType();
        int points = type.getBasePoints();
        if (System.currentTimeMillis() < doublePointsUntil) points *= 2;
        score += points;

        snake.grow(type.getGrowAmount());

        switch (type) {
            case SPEED_BOOST -> speedBoostUntil = System.currentTimeMillis() + type.getDurationMs();
            case DOUBLE_POINTS -> doublePointsUntil = System.currentTimeMillis() + type.getDurationMs();
            case INVINCIBILITY -> invincibilityUntil = System.currentTimeMillis() + type.getDurationMs();
            default -> {}
        }

        spawnNewFood();
    }

    private void spawnNewFood() {
        List<Point> allObstaclePoints = obstacles.stream()
                .flatMap(o -> o.getPositions().stream())
                .collect(Collectors.toList());
        food = Food.createRandom(GRID_WIDTH, GRID_HEIGHT, snake, allObstaclePoints, score);
    }

    private void gameOver() {
        state = GameState.GAME_OVER;
        if (timer != null) timer.stop();
        parentFrame.showGameOver(score, highScores.getHighScore());
        repaint();
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.currentDelay = difficulty.getDelayMs();
    }

    public void setWallMode(boolean wallMode) {
        this.wallMode = wallMode;
    }

    public void pauseGame() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
            if (timer != null) timer.stop();
        }
    }

    public void resumeGame() {
        if (state == GameState.PAUSED) {
            state = GameState.PLAYING;
            startTimer();
        }
    }

    public GameState getState() {
        return state;
    }

    public HighScoreManager getHighScoreManager() {
        return highScores;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void resetToMenu() {
        state = GameState.MENU;
        if (timer != null) timer.stop();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (state == GameState.MENU) {
            drawMenu(g2d);
            return;
        }

        drawGrid(g2d);
        drawObstacles(g2d);
        drawFood(g2d);
        drawSnake(g2d);
        drawHUD(g2d);

        if (state == GameState.PAUSED) {
            drawPauseOverlay(g2d);
        } else if (state == GameState.GAME_OVER) {
            drawGameOverOverlay(g2d);
        }
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(GRID_COLOR);
        for (int x = 0; x <= GRID_WIDTH; x++) {
            g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, getHeight());
        }
        for (int y = 0; y <= GRID_HEIGHT; y++) {
            g.drawLine(0, y * TILE_SIZE, getWidth(), y * TILE_SIZE);
        }
    }

    private void drawSnake(Graphics2D g) {
        List<Point> body = snake.getBody();
        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            int x = p.x * TILE_SIZE;
            int y = p.y * TILE_SIZE;
            if (i == 0) {
                g.setColor(SNAKE_HEAD);
                g.fillRoundRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2, 6, 6);
                g.setColor(SNAKE_HEAD.brighter());
                g.drawRoundRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2, 6, 6);
            } else {
                float alpha = 1f - (i * 0.02f);
                g.setColor(new Color(SNAKE_BODY.getRed(), SNAKE_BODY.getGreen(), SNAKE_BODY.getBlue(), (int)(255 * alpha)));
                g.fillRoundRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4, 4, 4);
            }
        }
    }

    private void drawFood(Graphics2D g) {
        Point pos = food.getPosition();
        int x = pos.x * TILE_SIZE;
        int y = pos.y * TILE_SIZE;

        Color color = switch (food.getType()) {
            case FOOD -> new Color(0xe74c3c);
            case GOLDEN -> new Color(0xf1c40f);
            case SPEED_BOOST -> new Color(0x3498db);
            case GROW -> new Color(0x2ecc71);
            case DOUBLE_POINTS -> new Color(0x9b59b6);
            case INVINCIBILITY -> new Color(0xe67e22);
        };

        g.setColor(color);
        g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
        g.setColor(color.brighter());
        g.drawOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
    }

    private void drawObstacles(Graphics2D g) {
        g.setColor(new Color(0x555555));
        for (Obstacle obs : obstacles) {
            for (Point p : obs.getPositions()) {
                int x = p.x * TILE_SIZE;
                int y = p.y * TILE_SIZE;
                g.fillRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2);
            }
        }
    }

    private void drawHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 20);

        long now = System.currentTimeMillis();
        if (now < speedBoostUntil) {
            g.setColor(new Color(0x3498db));
            g.drawString("SPEED!", getWidth() - 60, 20);
        }
        if (now < doublePointsUntil) {
            g.setColor(new Color(0x9b59b6));
            g.drawString("2x", getWidth() - 90, 20);
        }
        if (now < invincibilityUntil) {
            g.setColor(new Color(0xe67e22));
            g.drawString("SHIELD", getWidth() - 140, 20);
        }
    }

    private void drawPauseOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 36));
        String text = "PAUSED";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, getHeight() / 2 - 20);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g.drawString("Press SPACE to resume", (getWidth() - g.getFontMetrics().stringWidth("Press SPACE to resume")) / 2, getHeight() / 2 + 20);
    }

    private void drawGameOverOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(0xe74c3c));
        g.setFont(new Font("Segoe UI", Font.BOLD, 42));
        String text = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, getHeight() / 2 - 50);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        g.drawString("Score: " + score, (getWidth() - g.getFontMetrics().stringWidth("Score: " + score)) / 2, getHeight() / 2);
    }

    private void drawMenu(Graphics2D g) {
        g.setColor(new Color(0x4ecca3));
        g.setFont(new Font("Segoe UI", Font.BOLD, 48));
        String title = "SNAKE";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, getHeight() / 3);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        g.setColor(Color.WHITE);
        g.drawString("High Score: " + highScores.getHighScore(), (getWidth() - g.getFontMetrics().stringWidth("High Score: " + highScores.getHighScore())) / 2, getHeight() / 2);
        g.drawString("Press SPACE to start", (getWidth() - g.getFontMetrics().stringWidth("Press SPACE to start")) / 2, getHeight() / 2 + 40);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (state == GameState.MENU) {
            if (key == KeyEvent.VK_SPACE) {
                parentFrame.showGameSetup();
            }
            return;
        }
        if (state == GameState.GAME_OVER) return;

        if (key == KeyEvent.VK_SPACE) {
            if (state == GameState.PAUSED) resumeGame();
            else if (state == GameState.PLAYING) pauseGame();
            return;
        }

        if (state != GameState.PLAYING) return;

        switch (key) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> snake.setDirection(Direction.UP);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> snake.setDirection(Direction.DOWN);
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> snake.setDirection(Direction.LEFT);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> snake.setDirection(Direction.RIGHT);
            case KeyEvent.VK_ESCAPE -> pauseGame();
            default -> {}
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
