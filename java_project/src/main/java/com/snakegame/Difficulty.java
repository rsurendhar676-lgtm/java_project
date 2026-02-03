package com.snakegame;

/**
 * Game difficulty levels affecting speed and obstacle density.
 */
public enum Difficulty {
    EASY(120, 0, "Easy"),
    MEDIUM(90, 3, "Medium"),
    HARD(65, 6, "Hard"),
    EXTREME(45, 10, "Extreme");

    private final int delayMs;
    private final int obstacleCount;
    private final String displayName;

    Difficulty(int delayMs, int obstacleCount, String displayName) {
        this.delayMs = delayMs;
        this.obstacleCount = obstacleCount;
        this.displayName = displayName;
    }

    public int getDelayMs() {
        return delayMs;
    }

    public int getObstacleCount() {
        return obstacleCount;
    }

    public String getDisplayName() {
        return displayName;
    }
}
