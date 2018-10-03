package novruzov.nazar.snakegame;

import novruzov.nazar.snakegame.snake.Snake;
import novruzov.nazar.snakegame.snake.SnakeDirection;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Board {
    private int width;
    private int height;
    private Snake snake;
    private Mouse mouse;

    public static Board game;

    public Board(int width, int height, Snake snake) {
        this.width = width;
        this.height = height;
        this.snake = snake;
        game = this;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static void main(String[] args) throws IOException {
        game = initializeBoard();
        game.snake.setDirection(SnakeDirection.DOWN);
        game.createMouse();
        game.run();
    }

    private static Board initializeBoard() throws IOException {
        boolean repeatLoop = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the board's width(0-50):");
        int width = 0;

        do {
            try {
                width = Integer.parseInt(reader.readLine());
                if (width >= 0 && width <= 50) {
                    repeatLoop = false;
                } else {
                    System.out.println("Please enter the board's width(0-50):");
                }
            } catch (NumberFormatException e) {
                System.out.println("Only numbers are permitted! Try again.");
            }
        } while (repeatLoop);


        System.out.println("Please enter the board's height(0-50):");
        int height = 0;
        repeatLoop = true;
        do {
            try {
                height = Integer.parseInt(reader.readLine());
                if (height >= 0 && height <= 50) {
                    repeatLoop = false;
                } else {
                    System.out.println("Please enter the board's height(0-50):");
                }
            } catch (NumberFormatException e) {
                System.out.println("Only numbers are permitted! Try again.");
            }
        } while (repeatLoop);

        System.out.println("Please enter the snake's starting x position(0-50):");
        int headX = 0;
        repeatLoop = true;
        do {
            try {
                headX = Integer.parseInt(reader.readLine());
                if (headX >= 0 && headX <= 50) {
                    repeatLoop = false;
                } else {
                    System.out.println("Please enter the snake's starting x position(0-50):");
                }
            } catch (NumberFormatException e) {
                System.out.println("Only numbers are permitted! Try again.");
            }
        } while (repeatLoop);


        System.out.println("Please enter the snake's starting y position(0-50):");
        int headY = 0;
        repeatLoop = true;
        do {
            try {
                headY = Integer.parseInt(reader.readLine());
                if (headY >= 0 && headY <= 50) {
                    repeatLoop = false;
                } else {
                    System.out.println("Please enter the snake's starting y position(0-50):");
                }
            } catch (NumberFormatException e) {
                System.out.println("Only numbers are permitted! Try again.");
            }
        } while (repeatLoop);

        return new Board(width, height, new Snake(headX, headY));
    }

    /**
     * the main program logic
     */
    public void run() {
        KeyboardObserver keyboardObserver = new KeyboardObserver();
        keyboardObserver.start();

        while (snake.isAlive()) {
            if (keyboardObserver.hasKeyEvents()) {
                KeyEvent event = keyboardObserver.getEventFromTop();
                if (event.getKeyChar() == 'q') {
                    System.exit(1);
                    return;
                }

                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                    snake.setDirection(SnakeDirection.LEFT);
                else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                    snake.setDirection(SnakeDirection.RIGHT);
                else if (event.getKeyCode() == KeyEvent.VK_UP)
                    snake.setDirection(SnakeDirection.UP);
                else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                    snake.setDirection(SnakeDirection.DOWN);
            }

            snake.move();
            print();
            sleep();
        }

        System.out.println("Game Over!");
    }

    public void print() {
        char board[][] = new char[this.height][this.width];
        int headX = this.snake.getHeadX();
        int headY = this.snake.getHeadY();

        int mouseX = this.mouse.getX();
        int mouseY = this.mouse.getY();

        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board[i].length; k++) {
                printSnakeBody(board, i, k);
                if (i == headY && k == headX) {
                    board[i][k] = 'X';
                } else if (i == mouseY && k == mouseX) {
                    board[i][k] = '^';
                } else if (board[i][k] != 'x') {
                    board[i][k] = '.';
                }
                System.out.print(board[i][k] + " ");
            }
            System.out.println();
        }
    }

    private void printSnakeBody(char[][] board, int i, int k) {
        int snakeBodySize = this.snake.getSections()
                .subList(1, this.snake.getSections().size())
                .size();

        if (snakeBodySize == 0) {
            return;
        }

        for (int j = 0; j < snakeBodySize; j++) {
            int bodyX = this.snake.getSections().get(j + 1).getX();
            int bodyY = this.snake.getSections().get(j + 1).getY();

            if (i == bodyY && k == bodyX) {
                board[i][k] = 'x';
            }
        }
    }

    public void eatMouse() {
        createMouse();
    }

    public void createMouse() {
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);

        mouse = new Mouse(x, y);
    }

    public void sleep() {
        int level = this.snake.getSections().size();
        try {
            if (level > 0 && level <= 10) {
                Thread.sleep(500);
            } else if (level > 10 && level < 15) {
                Thread.sleep(300);
            } else {
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
