package com.snakegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the snake entity with segments and movement logic.
 */
public class Snake {
    private final LinkedList<Point> body;
    private Direction direction;
    private Direction pendingDirection;

    public Snake(int startX, int startY) {
        this.body = new LinkedList<>();
        this.body.add(new Point(startX, startY));
        this.body.add(new Point(startX - 1, startY));
        this.body.add(new Point(startX - 2, startY));
        this.direction = Direction.RIGHT;
        this.pendingDirection = Direction.RIGHT;
    }

    public void setDirection(Direction newDirection) {
        if (newDirection != null && !newDirection.isOpposite(direction)) {
            this.pendingDirection = newDirection;
        }
    }

    public void move() {
        this.direction = pendingDirection;
        Point head = body.getFirst();
        Point offset = direction.getOffset();
        Point newHead = new Point(head.x + offset.x, head.y + offset.y);
        body.addFirst(newHead);
        body.removeLast();
    }

    public void grow(int amount) {
        for (int i = 0; i < amount; i++) {
            Point tail = body.getLast();
            body.addLast(new Point(tail));
        }
    }

    public Point getHead() {
        return body.getFirst();
    }

    public List<Point> getBody() {
        return new ArrayList<>(body);
    }

    public int getLength() {
        return body.size();
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean contains(Point point) {
        return body.contains(point);
    }

    public boolean collidesWithSelf() {
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }

    /** Wraps head position for wraparound mode. */
    public void wrapHead(int gridWidth, int gridHeight) {
        Point head = body.getFirst();
        int x = ((head.x % gridWidth) + gridWidth) % gridWidth;
        int y = ((head.y % gridHeight) + gridHeight) % gridHeight;
        body.set(0, new Point(x, y));
    }
}
