import java.util.Random;

public class Apple1 implements Apple{

    int posX;
    int posY;

    Apple1(int X, int Y) {
        posX = X;
        posY = Y;
    }

    public void random() {
        posX = new Random().nextInt(19);
        posY = new Random().nextInt(19);
    }
}