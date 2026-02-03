package com.snakegame;

import java.awt.Point;
import java.util.List;
import java.util.Random;

/**
 * Represents food or power-up that appears on the game grid.
 */
public class Food {
    private Point position;
    private PowerUpType type;
    private final Random random = new Random();

    public Food(Point position, PowerUpType type) {
        this.position = position;
        this.type = type;
    }

    public static Food createRandom(int gridWidth, int gridHeight, Snake snake,
                                    List<Point> obstacles, int score) {
        PowerUpType type = selectPowerUpType(score);
        Point pos = findValidPosition(gridWidth, gridHeight, snake, obstacles);
        return new Food(pos, type);
    }

    private static PowerUpType selectPowerUpType(int score) {
        // Higher score = more chance for special power-ups
        double roll = Math.random();
        if (score > 500 && roll < 0.15) return PowerUpType.GOLDEN;
        if (score > 200 && roll < 0.25) return PowerUpType.SPEED_BOOST;
        if (score > 300 && roll < 0.30) return PowerUpType.GROW;
        if (score > 400 && roll < 0.35) return PowerUpType.DOUBLE_POINTS;
        if (score > 600 && roll < 0.40) return PowerUpType.INVINCIBILITY;
        return PowerUpType.FOOD;
    }

    private static Point findValidPosition(int gridWidth, int gridHeight,
                                          Snake snake, List<Point> obstacles) {
        Random r = new Random();
        Point pos;
        int attempts = 0;
        do {
            pos = new Point(r.nextInt(gridWidth), r.nextInt(gridHeight));
            attempts++;
        } while ((snake.contains(pos) || obstacles.contains(pos)) && attempts < 1000);
        return pos;
    }

    public Point getPosition() {
        return position;
    }

    public PowerUpType getType() {
        return type;
    }
}
