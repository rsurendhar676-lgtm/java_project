package com.snakegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Obstacles that the snake must avoid.
 */
public class Obstacle {
    private final List<Point> positions;
    private final Random random = new Random();

    public Obstacle(List<Point> positions) {
        this.positions = new ArrayList<>(positions);
    }

    public static List<Obstacle> generateObstacles(int count, int gridWidth, int gridHeight,
                                                   Snake snake, Point foodPos) {
        List<Obstacle> obstacles = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < count; i++) {
            List<Point> block = new ArrayList<>();
            int blockSize = 1 + r.nextInt(3);
            Point start = findValidStart(gridWidth, gridHeight, snake, foodPos, obstacles);

            for (int j = 0; j < blockSize; j++) {
                int dx = r.nextInt(3) - 1;
                int dy = r.nextInt(3) - 1;
                Point p = new Point(start.x + dx, start.y + dy);
                if (p.x >= 0 && p.x < gridWidth && p.y >= 0 && p.y < gridHeight
                        && !snake.contains(p) && !p.equals(foodPos)) {
                    block.add(p);
                }
            }
            if (!block.isEmpty()) {
                obstacles.add(new Obstacle(block));
            }
        }
        return obstacles;
    }

    private static Point findValidStart(int gridWidth, int gridHeight, Snake snake,
                                        Point foodPos, List<Obstacle> existing) {
        Random r = new Random();
        Point p;
        int attempts = 0;
        do {
            p = new Point(r.nextInt(gridWidth), r.nextInt(gridHeight));
            attempts++;
        } while ((snake.contains(p) || p.equals(foodPos) || isInObstacles(p, existing))
                && attempts < 500);
        return p;
    }

    private static boolean isInObstacles(Point p, List<Obstacle> obstacles) {
        for (Obstacle obs : obstacles) {
            if (obs.positions.contains(p)) return true;
        }
        return false;
    }

    public List<Point> getPositions() {
        return new ArrayList<>(positions);
    }
}
