package breakout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
	public int map[][];
	public int brickHeight;
	public int brickWidth;
	
	public MapGenerator (int row, int col) {
		map = new int[row][col];
		for (int i=0;i<map.length;i++) {
			for (int j=0;j<map[0].length;j++) {
				map[i][j] = map.length - i;
			}
		}
		
		brickWidth = 540/col;
		brickHeight = 150/row;
	}
	
	public void draw(Graphics2D g) {
		for (int i=0;i<map.length;i++) {
			for (int j=0;j<map[0].length;j++) {
				
				if (map[i][j] > 0) {
					
					switch (map[i][j]) {
					
					case 1: g.setColor(new Color(46, 100, 217));
							break;
					case 2: g.setColor(new Color(160, 57, 219));
							break;		
					case 3: g.setColor(new Color(194, 55, 33));
							break;
					case 4: g.setColor(new Color(209, 135, 17));
							break;
					case 5: g.setColor(new Color(209, 209, 15));
							break;
					default: g.setColor(Color.WHITE);		
					}
					
					g.fillRect(j*brickWidth+80,i*brickHeight+50, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.BLACK);
					g.drawRect(j*brickWidth+80,i*brickHeight+50, brickWidth, brickHeight);
				}
			}
		}
	}
	
	public void setBrickValue (int row, int col) {
		map[row][col] -= 1;
	}
	
	public int getBrickValue (int row, int col) {
		return map[row][col];
	}

}
