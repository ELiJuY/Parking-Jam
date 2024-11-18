package sounds;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


import org.apache.log4j.Logger;



public class Sound {

    private Clip clip;
    private AudioInputStream audioInput;
    private static final Logger logger = Logger.getLogger(Sound.class);

   
    public Sound(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (soundFile.exists()) {
                audioInput = AudioSystem.getAudioInputStream(soundFile);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
            } else {
            	logger.error("The sound file: "+ filePath + " doesn't exist");
            }
        }  catch (Exception e) {
        	logger.error("Error loading the music: "+ filePath);
        }
    }

   
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); 
            clip.start();
        }
    }

   
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.flush();
            clip.setFramePosition(0); 
        }
    }

    
    public void close() {
        if (clip != null) {
            clip.stop();
            clip.flush();
            clip.close();
            try {
                audioInput.close();
            } catch (IOException e) {
            	logger.error("Error closing the input stream");
            }
        }
    }

  
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
