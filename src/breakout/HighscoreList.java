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
	
	private int[] scoresArray;

	public int[] getScores (int score) {
		
		List<String> scores = new ArrayList<String>();	
		
		try	{	
			//inputFile = new File("highscores.txt");
			BufferedReader reader = new BufferedReader(new FileReader("highscores.txt"));
			String currentLine;
			
			while((currentLine = reader.readLine()) != null) {
				scores.add(currentLine);
			}
			
			reader.close();
			
			scores.add(Integer.toString(score));
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
	
	public void writeScores () {
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("highscores.txt"));
			
			for (int i=scoresArray.length-1;i>=1;i--) {
				System.out.println(Integer.toString(scoresArray[i]));
				writer.write(Integer.toString(scoresArray[i]));
				writer.newLine();
			}
			
			writer.close();
				
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
}
