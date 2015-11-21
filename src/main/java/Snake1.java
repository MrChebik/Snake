/**
 * Описание змейки
 * @since 0.1
 */
public class Snake1 implements Snake{

    /**
     * Направление
     */
    public int direction = 0;

    public int length = 2;

    /**
     * Максимальная длина по горизонтали и вертикали
     */
    public final int snakeX[] = new int[200];
    public final int snakeY[] = new int[200];

    Snake1(int x0, int y0, int x1, int y1) {
        snakeX[0] = x0;
        snakeY[0] = y0;
        snakeX[1] = x1;
        snakeY[1] = y1;
    }

    /**
     * Движение змеи
     */
    public void move() {
        /**
         * Хвост укорачивается
         */
        for (int d = length; d > 0; d--) {
            snakeX[d] = snakeX[d - 1];
            snakeY[d] = snakeY[d - 1];
        }

        /**
         * По направлению, голова передвигается на 1 клетку вперед/назад
         */
        if (direction == 0) snakeX[0]++;
        if (direction == 1) snakeY[0]++;
        if (direction == 2) snakeX[0]--;
        if (direction == 3) snakeY[0]--;

        if (length < 2) length = 2;
    }

    /**
     * Если змея съедает хвост, то длина равняется оставшейся - 2
     * *Действует только в режиме: Unlimited
     */
    public void eats() {
        for (int d = length; d > 0; d--)
            if (snakeX[0] == snakeX[d] && snakeY[0] == snakeY[d]) length = d - 2;
    }

    /**
     * Зеркало, если выходить за поля
     */
    public void mirror() {
        if (snakeX[0] > 19) snakeX[0] = 0;
        if (snakeX[0] < 0) snakeX[0] = 19;
        if (snakeY[0] > 19) snakeY[0] = 0;
        if (snakeY[0] < 0) snakeY[0] = 19;
    }

    /**
     * Назад, если GAME OVER
     * *Иначе, голова змеи была бы:
     *  -на хвосте
     *  -за полем
     * @since 0.3
     */
    public void back() {
        for (int d = 0; d < length; d++) {
            snakeX[d] = snakeX[d + 1];
            snakeY[d] = snakeY[d + 1];
        }
    }
}