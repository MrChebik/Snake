/**
 * Created by alex on 11.11.15.
 */
import java.util.Random;

public class Apple2 implements Apple{

    int posX;
    int posY;

    Apple2(int X, int Y) {
        posX = X;
        posY = Y;
    }

    public void random() {
        posX = new Random().nextInt(19);
        posY = new Random().nextInt(19);
    }
}