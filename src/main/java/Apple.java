import java.util.Random;

public class Apple {

    int posX;
    int posY;

    Apple(int X, int Y) {
        posX = X;
        posY = Y;
    }

    void random() {
        posX = new Random().nextInt(19);
        posY = new Random().nextInt(19);
    }
}