package com.snakegame;

/**
 * Types of power-ups that can appear in the game.
 */
public enum PowerUpType {
    /** Standard food - increases score and snake length */
    FOOD(100, 1, 0, false, 0),
    /** Golden food - double points */
    GOLDEN(200, 1, 0, false, 0),
    /** Speed boost - temporarily increases game speed */
    SPEED_BOOST(50, 1, 0, true, 5000),
    /** Grow - instant +3 segments */
    GROW(150, 3, 0, false, 0),
    /** Double points - 2x score for a duration */
    DOUBLE_POINTS(75, 1, 0, true, 8000),
    /** Invincibility - no death from walls/self for duration */
    INVINCIBILITY(100, 1, 0, true, 6000);

    private final int basePoints;
    private final int growAmount;
    private final int shrinkAmount;
    private final boolean temporary;
    private final int durationMs;

    PowerUpType(int basePoints, int growAmount, int shrinkAmount, boolean temporary, int durationMs) {
        this.basePoints = basePoints;
        this.growAmount = growAmount;
        this.shrinkAmount = shrinkAmount;
        this.temporary = temporary;
        this.durationMs = durationMs;
    }

    public int getBasePoints() {
        return basePoints;
    }

    public int getGrowAmount() {
        return growAmount;
    }

    public int getShrinkAmount() {
        return shrinkAmount;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public int getDurationMs() {
        return durationMs;
    }
}
