package breakout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Bricks {
	public int map[][];
	public int brickHeight;
	public int brickWidth;
	
	public Bricks (int row, int col) {
		map = new int[row][col];
		for (int i=0;i<map.length;i++) {
			for (int j=0;j<map[0].length;j++) {
				map[i][j] = map.length - i; // oberste Reihe hat map.length "Lebenspunkte"
			}
		}	
		// Die Bricks werden immer im gleichen Bereich dargestellt, egal wie viele es sind
		brickWidth = 540/col;
		brickHeight = 150/row;
	}
	
	public void draw(Graphics2D g) {
	// Zeichnen der Bricks, wird von BreakoutInterface.paint() gecallt
		for (int i=0;i<map.length;i++) {
			for (int j=0;j<map[0].length;j++) {	
				if (map[i][j] > 0) {	
					switch (map[i][j]) {
					
					case 1: g.setColor(new Color(46, 100, 217)); // blue
							break;
					case 2: g.setColor(new Color(160, 57, 219)); // purple
							break;		
					case 3: g.setColor(new Color(194, 55, 33)); // red
							break;
					case 4: g.setColor(new Color(209, 135, 17)); // orange
							break;
					case 5: g.setColor(new Color(209, 209, 15)); // yellow
							break;
					default: g.setColor(Color.WHITE);		
					}
					
					g.fillRect(j*brickWidth+80,i*brickHeight+50, brickWidth, brickHeight);
					// Umrandung der Bricks
					g.setStroke(new BasicStroke(3)); 
					g.setColor(Color.BLACK);
					g.drawRect(j*brickWidth+80,i*brickHeight+50, brickWidth, brickHeight);
				}
			}
		}
	}
	
	public void setBrickValue (int row, int col) {
		map[row][col] -= 1; // Lebenspunkte verlieren -> Farbe wird geändert
	}
	
	public int getBrickValue (int row, int col) {
		return map[row][col];
	}
}
