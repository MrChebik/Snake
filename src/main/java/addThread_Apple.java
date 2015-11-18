import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.InputStream;

/**
 * Created by alex on 11.11.15.
 */

/**
 * Поток для проигрывания mp3 файла
 * @author MrChebik
 * @since 0.4
 */
public class addThread_Apple extends Thread {
    public void run() {
        try {

            /**
             * Берется мультимедийный файл с jar архива
             * @autor Vaysman
             * @since 0.4
             * {@link http://vk.com/mikhail.vaysman}
             */
            InputStream inputStream = Main.class.getResourceAsStream("/res/sound/Eating_Apple.mp3");

            AudioDevice audioDevice = new JavaSoundAudioDevice();
            AdvancedPlayer player = new AdvancedPlayer(inputStream, audioDevice);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
