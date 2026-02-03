# Advanced Snake Game

A feature-rich Snake game built with Java Swing.

## Features

- **Multiple difficulty levels**: Easy, Medium, Hard, Extreme
- **Power-ups**:
  - 🍎 Food (standard)
  - ⭐ Golden (double points)
  - ⚡ Speed Boost (temporary)
  - 🌱 Grow (+3 segments)
  - 💜 Double Points (2x score for 8 seconds)
  - 🛡️ Invincibility (no death for 6 seconds)
- **Obstacles** on Medium, Hard, and Extreme
- **Wall mode** toggle (classic walls vs wraparound)
- **High score** persistence
- **Pause** with SPACE, resume with SPACE
- **WASD or Arrow keys** for movement

## Requirements

- Java 17+

## Build & Run

```bash
# Build
mvn clean package

# Run
mvn exec:java -Dexec.mainClass="com.snakegame.SnakeGame"
```

Or run the JAR:

```bash
java -jar target/snake-game-advanced-1.0.0.jar
```

## Controls

| Key | Action |
|-----|--------|
| W / ↑ | Move up |
| S / ↓ | Move down |
| A / ← | Move left |
| D / → | Move right |
| SPACE | Pause / Resume |
| ESC | Pause |

## Project Structure

```
src/main/java/com/snakegame/
├── Main.java           - Entry point
├── SnakeGame.java      - Main frame
├── GamePanel.java      - Game logic & rendering
├── GameSetupDialog.java
├── GameOverDialog.java
├── Snake.java
├── Food.java
├── Obstacle.java
├── Direction.java
├── PowerUpType.java
├── Difficulty.java
├── GameState.java
└── HighScoreManager.java
```
