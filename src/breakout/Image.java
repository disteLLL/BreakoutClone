package breakout;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {

	private static BufferedImage image;
	
	public static BufferedImage getImage (String fileName) {
	// liefert die Bilddateien, für die PowerUps, als Image-Objekt	
		try {
			image = ImageIO.read(new File(fileName));	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;	
	}
}
