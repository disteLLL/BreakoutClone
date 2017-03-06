package breakout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BreakoutInterface extends JPanel implements ActionListener, KeyListener {

	public int score = 0; // Parameter für HighscoreList.getScores(score);
	private String[] placings = {"","10:","09:","08:","07:","06:","05:","04:","03:","02:","01:"}; // für die Darstellung der HighscoreList
	private String[] powerUps = {"faster","slower","wider","narrower"}; // Dateinamen der PowerUps
	private String imageName;
	private Random random;
	private Timer timer; // Swing-Timer
	private Bricks bricks;
	// flags für den KeyListener
	private boolean rightArrow; 
	private boolean leftArrow;
	// Initialparameter
	private boolean play = false;
	private boolean powerUpCheck = false;
	private boolean scoreCheck = true;
	private int rows = 2;
	private int cols = 7;
	private int delay = 20;
	private int playerX = 310;
	private int barWidth = 100;
	private int barWidthSide = 40;
	private int barRightX = 60;
	private int ballposX = 240;
	private int ballposY = 220;
	private int ballXdir = 3; // Modifier für ballposX
	private int ballYdir = 6; // Modifier für ballposY
	private int totalBricks = rows * cols;
	private int powerUpY;
	private int powerUpX;
	
	public BreakoutInterface() {
		bricks = new Bricks(rows, cols);
		Sound.playClip("resources/background.wav"); // Hintergrundmusik (geloopt)
		addKeyListener(this);
		setFocusable(true);
		random = new Random (); // für die PowerUp-Wahrscheinlichkeit
		timer = new Timer(delay, this); // feuert ActionEvents mit dem Abstand delay (millisec)
		timer.start();
	}

	public void paint(Graphics g) {
		// zuständig für das Zeichnen auf dem JPanel, wird von actionPerformed() gecallt
		
		// background
		g.setColor(Color.BLACK);
		g.fillRect(1, 1, 692, 592);

		// bricks
		bricks.draw((Graphics2D) g);

		// borders
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);

		// score
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		g.drawString("" + score, 10, 30);

		// bar
		g.setColor(Color.WHITE);
		g.fillRect(playerX, 550, barWidth, 8);

		// ball
		g.setColor(Color.WHITE);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		// powerups
		if (powerUpCheck && powerUpY < 570) { // nie mehr als ein PowerUp zur Zeit
			g.drawImage(Image.getImage("resources/"+imageName+".png"), powerUpX, powerUpY, null);
			powerUpY += 3; // PowerUp "fällt" nach unten
		}
		
		// game over
		if (ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			
			if (scoreCheck) gameOver(); // einmaliger Aufruf von gameOver()
			
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
			g.drawString("Scores:", 5, 300);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
			int offset = 0;
			
			// Darstellung der Scores in absteigender Reihenfolge
			for (int i=HighscoreList.scoresArray.length-1;i>=1;i--) {
				g.drawString(placings[i], 10, 330+25*offset);
				g.drawString(Integer.toString(HighscoreList.scoresArray[i]), 50, 330+25*offset);
				offset++;
			}
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
			g.setColor(Color.RED);
			g.drawString("Game Over", 265, 300);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 240, 350);
			g.drawString("Press Space to Continue", 228, 400);
		}
			
		// continue
		if (totalBricks == 0 && ballposY < 570) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
			g.drawString("Map finished!", 240, 400);
			g.drawString("Catch the ball to continue", 165, 450);
		}
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// flüssige Bewegung der Bar solange die flags true sind
		if (leftArrow) {
			moveLeft();
		}
		if (rightArrow) {
			moveRight();
		}
		
		if (play) {
			// Hitboxes für Bar, Ball und PowerUps
			Rectangle barRight = new Rectangle(playerX + barRightX, 550, barWidthSide, 8);
			Rectangle barLeft = new Rectangle(playerX, 550, barWidthSide, 8);
			Rectangle bar = new Rectangle(playerX, 550, barWidth, 8);
			Rectangle ball = new Rectangle(ballposX, ballposY, 20, 20);
			Rectangle powerUp = new Rectangle(powerUpX,powerUpY,48,48);

			//Kollisionsabfrage
			if (ball.intersects(bar)) {
				
				Sound.playClip("resources/nes-10-00.wav");
				
				// Level geschafft, eine Reihe dazu
				if (totalBricks == 0) {
					rows++; 
					totalBricks = rows * cols;
					bricks = new Bricks(rows, cols);
				}
				// Modifizieren der ballXdir um verschiedene Ballwinkel zu erreichen 	
				if (ball.intersects(barLeft)) {
					if (ballXdir < 0 && ballXdir > -9) {
						ballXdir-=3;
					}
					if (ballXdir > 3) {
						ballXdir-=3;
					}
				}
				if (ball.intersects(barRight)) {
					if (ballXdir < -3) {
						ballXdir+=3;
					}
					if (ballXdir > 0 && ballXdir < 9) {
						ballXdir+=3;
					}
				}
				if (ballXdir == -9 || ballXdir == 9) {
					ballYdir = 3;
				} else {
					ballYdir = 6;
				}
				ballYdir = -ballYdir; // Einfallswinkel = Ausfallswinkel
			}
			
			// PowerUp eingesammelt
			if (powerUp.intersects(bar)) {
				powerUpY = 600;
				powerUpBehaviour(imageName);
				powerUpCheck = false;
				score += 5000;
			}
			// PowerUp verfehlt
			if (powerUpY > 570) powerUpCheck = false;
			
			// Brickkollisionen
			outerloop:
			for (int i = 0; i < bricks.map.length; i++) {
				for (int j = 0; j < bricks.map[0].length; j++) {
					if (bricks.map[i][j] > 0) {
						// Brickhitboxen
						int brickX = j * bricks.brickWidth + 80;
						int brickY = i * bricks.brickHeight + 50;
						int brickWidth = bricks.brickWidth;
						int brickHeight = bricks.brickHeight;
						Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

						if (ball.intersects(brickRect)) {
							// erlaubt Darstellung eines zufälligen PowerUps mit einer Wahrscheinlichkeit von 30%	
							if (random.nextInt(10)+1 > 7) {
								if (!powerUpCheck) {		
									imageName = powerUps[random.nextInt(4)];
									powerUpCheck(imageName); // Darstellung erfolgt nur wenn dieser Check true liefert
									powerUpX = ballposX;
									powerUpY = ballposY;
								}	
							}
							
							bricks.setBrickValue(i, j); 
							// Brick zerstört
							if (bricks.getBrickValue(i, j) == 0) {
								Sound.playClip("resources/sfx_sounds_pause7_out.wav");
								totalBricks--;
								score += 25200 / (i + 1);
							}
							// Brick hat noch "Leben"
							else {
								Sound.playClip("resources/sfx_sounds_pause7_in.wav");
							}
							score += 1000;
							// Ball trifft Brick an den Seiten
							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							} 
							// Ball trifft Brick von oben oder unten
							else {
								ballYdir = -ballYdir;
							}
							
							break outerloop; // verhindert Multikollisionen
						}
					}
				}
			}
			
			// Bewegung des Balls
			ballposX += ballXdir;
			ballposY += ballYdir;
			// Abprallen an den seitlichen bzw. oberen Rändern
			if (ballposX < 0) {
				ballposX = 0; // verhindert Wallstuck
				Sound.playClip("resources/sfx_sounds_interaction3.wav");
				ballXdir = -ballXdir;
			}
			if (ballposY < 0) {
				Sound.playClip("resources/sfx_sounds_interaction3.wav");
				ballYdir = -ballYdir;
			}
			if (ballposX > 670) {
				ballposX = 670; // verhindert Wallstuck
				Sound.playClip("resources/sfx_sounds_interaction3.wav");
				ballXdir = -ballXdir;
			}
		}
		repaint(); // callt paint()
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightArrow = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftArrow = true;
		}
		// restart
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {		
				rows = 2;
				reset();
			}
		}
		// continue
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (!play) {		
				reset();
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightArrow = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftArrow = false;
		}
	}

	public void moveRight() {
		play = true;
		// verhindert "aus der Map fahren"
		if (700-playerX <= barWidth) {
			playerX = 700-barWidth;
		} else {
			playerX += 10;
		}	
	}

	public void moveLeft() {
		play = true;
		// verhindert "aus der Map fahren"
		if (playerX <= 0) {
			playerX = 0;
		} else {
			playerX -= 10;
		}
	}
		
	public void powerUpBehaviour (String powerUp) {
	// Verhalten beim Einsammeln der PowerUps
		if (powerUp.equals("wider")) {	
			Sound.playClip("resources/sfx_sound_neutral4.wav");
			barWidth += 20;
			double temp = barWidth*0.4;
			barWidthSide = (int) temp;
			double temp2 = barWidth*0.6;
			barRightX = (int) temp2;
		}
		if (powerUp.equals("narrower")) {
			Sound.playClip("resources/sfx_sound_neutral4.wav");
			barWidth -= 20;
			double temp = barWidth*0.4;
			barWidthSide = (int) temp;
			double temp2 = barWidth*0.6;
			barRightX = (int) temp2;
		}
		if (powerUp.equals("faster")) {
			Sound.playClip("resources/sfx_sound_neutral10.wav");
			delay -= 5;
			timer.setDelay(delay);
		}
		if (powerUp.equals("slower")) {
			Sound.playClip("resources/sfx_sound_neutral10.wav");
			delay += 5;
			timer.setDelay(delay);
		}
	}
	
	public void powerUpCheck (String powerUp) {	
	// Check ob PowerUp erlaubt ist	
		if (powerUp.equals("wider")) {
			if (barWidth < 140) {
				powerUpCheck = true;
			}
		}
		if (powerUp.equals("narrower")) {
			if (barWidth > 60) {
				powerUpCheck = true;
			}
		}
		if (powerUp.equals("faster")) {
			if (delay > 15) {
				powerUpCheck = true;
			}
		}
		if (powerUp.equals("slower")) {
			if (delay < 20) {
				powerUpCheck = true;
			}
		}
	}

	public void gameOver() {
		// aktualisiert die Highscoreliste mit dem letzten Spielergebnis
		scoreCheck = false;
		HighscoreList.getScores(score);
		HighscoreList.writeScores();
	}
	
	public void reset() {
	// Variablen zurück auf Default
		play = true;
		scoreCheck = true;
		ballposX = 240;
		ballposY = 220;
		ballXdir = 3;
		ballYdir = 6;
		playerX = 310;
		barWidth = 100;
		barWidthSide = 40;
		barRightX = 60;
		score = 0;
		totalBricks = rows * cols;
		bricks = new Bricks(rows, cols);
		delay = 20;
		timer.setDelay(delay);
		repaint();
	}
		
	@Override
	public void keyTyped(KeyEvent e) {}
}
