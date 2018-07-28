/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import java.io.*;
import sun.audio.*;

public class Sound {

    private final String soundPath;
    private AudioStream audioStream;

    Sound(String soundPath) {
        this.soundPath = soundPath;
    }

    public void playSound() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(soundPath);
            audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
        }
    }
 
    public void stopSound() {
        AudioPlayer.player.stop(audioStream);
    }
}
