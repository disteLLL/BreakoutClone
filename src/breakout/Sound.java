package breakout;

import java.io.*;
import javax.sound.sampled.*;

public class Sound {
	
	private static Clip clip;
	
	public static void playClip (String fileName) {
	
		try {
		
			File clipFile = new File(fileName);
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;
			
			
			stream = AudioSystem.getAudioInputStream(clipFile);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			
			
			if (fileName.equals("resources/1.wav")) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				gainControl.setValue(-5.f);	
			}
			else {
				gainControl.setValue(-8.f);
			}
			
			clip.start();
			clip.drain();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}	

