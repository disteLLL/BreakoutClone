package breakout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HighscoreList {
	
	public static int[] scoresArray;

	public static int[] getScores (int score) {
		List<String> scores = new ArrayList<String>();	
		try	{
			// Auslesen der Highscores und Speicher in einer ArrayList
			BufferedReader reader = new BufferedReader(new FileReader("resources/highscores.txt"));
			String currentLine;
			
			while((currentLine = reader.readLine()) != null) {
				scores.add(currentLine);
			}
			reader.close();
			scores.add(Integer.toString(score));
			
			// ArrayList<String> wird in ein int-Array eingelesen und aufsteigend sortiert
			scoresArray = new int[scores.size()];
			for (int i = 0;i<scores.size();i++) {
				int temp = Integer.parseInt(scores.get(i));
				scoresArray[i] = temp;
			}
			Arrays.sort(scoresArray);	
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return scoresArray;
	}
	
	public static void writeScores () {
		try {
			// Top 10 scores werden absteigend in die highscores.txt geschrieben
			BufferedWriter writer = new BufferedWriter(new FileWriter("resources/highscores.txt"));
			for (int i=scoresArray.length-1;i>=1;i--) {
				writer.write(Integer.toString(scoresArray[i]));
				writer.newLine();
			}
			writer.close();		
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
}
