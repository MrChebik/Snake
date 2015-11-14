public class Snake {

    public int direction = 0;
    public int length = 2;
    public int snakeX[] = new int[200];
    public int snakeY[] = new int[200];

    Snake(int x0, int y0, int x1, int y1) {
        snakeX[0] = x0;
        snakeY[0] = y0;
        snakeX[1] = x1;
        snakeY[1] = y1;
    }

    void move() {
        for (int d = length; d > 0; d--) {
            snakeX[d] = snakeX[d - 1];
            snakeY[d] = snakeY[d - 1];
        }

        if (direction == 0) snakeX[0]++;
        if (direction == 1) snakeY[0]++;
        if (direction == 2) snakeX[0]--;
        if (direction == 3) snakeY[0]--;

        if (length < 2) length = 2;
    }

    void eats() {
        for (int d = length; d > 0; d--)
            if (snakeX[0] == snakeX[d] && snakeY[0] == snakeY[d]) length = d - 2;
    }

    void mirror() {
        if (snakeX[0] > 19) snakeX[0] = 0;
        if (snakeX[0] < 0) snakeX[0] = 19;
        if (snakeY[0] > 19) snakeY[0] = 0;
        if (snakeY[0] < 0) snakeY[0] = 19;
    }

    void back() {
        for (int d = 0; d < length; d++) {
            snakeX[d] = snakeX[d + 1];
            snakeY[d] = snakeY[d + 1];
        }
    }
}