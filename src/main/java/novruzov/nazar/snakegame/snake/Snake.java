package novruzov.nazar.snakegame.snake;

import novruzov.nazar.snakegame.Board;
import novruzov.nazar.snakegame.Mouse;

import java.util.LinkedList;
import java.util.List;

public class Snake {
    private SnakeDirection direction;
    private List<SnakeSection> sections;
    private boolean isAlive;

    public Snake(int headX, int headY) {
        this.sections = new LinkedList<SnakeSection>();
        this.sections.add(new SnakeSection(headX, headY));
        this.isAlive = true;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public int getHeadX() {
        if (this.sections != null && this.sections.size() > 0) {
            return this.sections.get(0).getX();
        }

        throw new IllegalStateException("Snake does not exist yet");
    }

    public int getHeadY() {
        if (this.sections != null && this.sections.size() > 0) {
            return this.sections.get(0).getY();
        }

        throw new IllegalStateException("Snake does not exist yet");
    }

    public void setDirection(SnakeDirection direction) {
        this.direction = direction;
    }

    public List<SnakeSection> getSections() {
        return this.sections;
    }

    public void move() {
        if (!this.isAlive) {
            return;
        }

        switch (this.direction) {
            case UP:
                move(0, -1);
                break;
            case DOWN:
                move(0, 1);
                break;
            case LEFT:
                move(-1, 0);
                break;
            case RIGHT:
                move(1, 0);
        }
    }

    private void move(int dx, int dy) {
        SnakeSection snakeHead = this.sections.get(0);
        snakeHead = new SnakeSection(snakeHead.getX() + dx, snakeHead.getY() + dy);

        checkBoarders(snakeHead);
        if (!this.isAlive) {
            return;
        }

        checkBody(snakeHead);
        if (!this.isAlive) {
            return;
        }

        Mouse mouse = Board.game.getMouse();

        if (snakeHead.getX() == mouse.getX() && snakeHead.getY() == mouse.getY()) {
            this.sections.add(0, snakeHead);
            Board.game.eatMouse();
        } else {
            this.sections.add(0, snakeHead);
            this.sections.remove(this.sections.size() - 1);
        }
    }

    private void checkBody(SnakeSection snakeHead) {
        if (this.sections.contains(snakeHead)) {
            this.isAlive = false;
        }
    }

    private void checkBoarders(SnakeSection snakeHead) {
        int snakeHeadX = snakeHead.getX();
        int snakeHeadY = snakeHead.getY();

        if (snakeHeadX < 0 || snakeHeadX >= Board.game.getWidth()
                || snakeHeadY < 0 || snakeHeadY >= Board.game.getHeight()) {
            this.isAlive = false;
        }
    }
}
