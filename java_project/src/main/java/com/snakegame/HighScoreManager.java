package com.snakegame;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Manages persistent high scores.
 */
public class HighScoreManager {
    private static final String SCORES_FILE = "highscores.dat";
    private final List<ScoreEntry> scores;
    private final Path filePath;

    public HighScoreManager() {
        this.scores = new ArrayList<>();
        this.filePath = Paths.get(System.getProperty("user.dir"), SCORES_FILE);
        load();
    }

    public void addScore(String playerName, int score, Difficulty difficulty) {
        scores.add(new ScoreEntry(playerName, score, difficulty, System.currentTimeMillis()));
        scores.sort((a, b) -> Integer.compare(b.score(), a.score()));
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear();
        }
        save();
    }

    public List<ScoreEntry> getTopScores() {
        return new ArrayList<>(scores);
    }

    public int getHighScore() {
        return scores.isEmpty() ? 0 : scores.get(0).score();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        try {
            if (Files.exists(filePath)) {
                try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                    List<ScoreEntry> loaded = (List<ScoreEntry>) ois.readObject();
                    scores.clear();
                    scores.addAll(loaded);
                }
            }
        } catch (Exception e) {
            scores.clear();
        }
    }

    private void save() {
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(scores);
            }
        } catch (IOException ignored) {
        }
    }

    public record ScoreEntry(String playerName, int score, Difficulty difficulty, long timestamp)
            implements Serializable {
    }
}
