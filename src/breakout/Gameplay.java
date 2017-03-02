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

public class Gameplay extends JPanel implements ActionListener, KeyListener {

	public int score = 0;
	private boolean play = false;
	private boolean scoreCheck = true;
	private boolean check = false;
	private boolean rightArrow;
	private boolean leftArrow;
	private int rows = 2;
	private int cols = 7;
	private int totalBricks = rows * cols;
	private int delay = 20;
	private int playerX = 310;
	private int barWidth = 100;
	private int barWidthSide = 40;
	private int barRightX = 60;
	private int ballposX = 240;
	private int ballposY = 220;
	private int ballXdir = 3;
	private int ballYdir = 6;
	private int[] scores;
	private int powerUpX;
	private int powerUpY = 600;
	private String[] placings = {"","10:","09:","08:","07:","06:","05:","04:","03:","02:","01:"};
	private String[] powerUps = {"faster","slower","wider","narrower"};
	private String imageName;
	private Random random;
	private Timer timer;
	private MapGenerator map;

	public Gameplay() {
		map = new MapGenerator(rows, cols);
		Sound.playClip("resources/background.wav");
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		random = new Random ();
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paint(Graphics g) {
		// background
		g.setColor(Color.BLACK);
		g.fillRect(1, 1, 692, 592);

		// drawing map
		map.draw((Graphics2D) g);

		// borders
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);

		// scores
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
		if (check && powerUpY < 570) {
			g.drawImage(Image.getImage("resources/"+imageName+".png"), powerUpX, powerUpY, null);
			powerUpY += 3;
		}
		
		// game over
		if (ballposY > 570) {
			
			if (scoreCheck) gameOver();
			
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
			g.drawString("Scores:", 5, 300);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
			int offset = 0;
			
			for (int i=scores.length-1;i>=1;i--) {
				g.drawString(placings[i], 10, 330+25*offset);
				g.drawString(Integer.toString(scores[i]), 50, 330+25*offset);
				offset++;
			}
			
			play = false;
			ballXdir = 0;
			ballYdir = 0;
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
		
		if (leftArrow) {
			moveLeft();
		}
		if (rightArrow) {
			moveRight();
		}
		
		if (play) {

			Rectangle barRight = new Rectangle(playerX + barRightX, 550, barWidthSide, 8);
			Rectangle barLeft = new Rectangle(playerX, 550, barWidthSide, 8);
			Rectangle bar = new Rectangle(playerX, 550, barWidth, 8);
			Rectangle ball = new Rectangle(ballposX, ballposY, 20, 20);
			Rectangle powerUp = new Rectangle(powerUpX,powerUpY,48,48);

			if (ball.intersects(bar)) {
				
				Sound.playClip("resources/nes-10-00.wav");
				
				if (totalBricks == 0) {
					rows++;
					totalBricks = rows * cols;
					map = new MapGenerator(rows, cols);
				}

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

				ballYdir = -ballYdir;
			}
			
			
			if (powerUp.intersects(bar)) {
				powerUpY = 600;
				powerUpBehaviour(imageName);
				check = false;
				score += 5000;
			}
			
			if (powerUpY > 570) check = false;
			
			

			A: for (int i = 0; i < map.map.length; i++) {
				for (int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

						if (ball.intersects(brickRect)) {
								
							if (random.nextInt(10)+1 > 5) {
								if (!check) {		
									imageName = powerUps[random.nextInt(4)];
									powerUpCheck(imageName);
									powerUpX = ballposX;
									powerUpY = ballposY;
								}	
							}
							
							map.setBrickValue(i, j);
							if (map.getBrickValue(i, j) == 0) {
								Sound.playClip("resources/sfx_sounds_pause7_out.wav");
								totalBricks--;
								score += 25200 / (i + 1);
							}
							else {
								Sound.playClip("resources/sfx_sounds_pause7_in.wav");
							}
							score += 1000;

							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							
							break A;
						}
					}
				}
			}

			ballposX += ballXdir;
			ballposY += ballYdir;

			if (ballposX < 0) {
				Sound.playClip("resources/sfx_sounds_interaction3.wav");
				ballXdir = -ballXdir;
			}
			if (ballposY < 0) {
				Sound.playClip("resources/sfx_sounds_interaction3.wav");
				ballYdir = -ballYdir;
			}
			if (ballposX > 670) {
				Sound.playClip("resources/sfx_sounds_interaction3.wav");
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightArrow = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftArrow = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {		
				rows = 2;
				reset();
			}
		}
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
		if (700-playerX <= barWidth) {
			playerX = 700-barWidth;
		} else {
			playerX += 10;
		}	
	}

	public void moveLeft() {
		
		play = true;
		if (playerX <= 0) {
			playerX = 0;
		} else {
			playerX -= 10;
		}
	}
		
	public void powerUpBehaviour (String powerUp) {
		
		if (powerUp.equals("wider")) {
			
			if (barWidth < 140) {
				Sound.playClip("resources/sfx_sound_neutral4.wav");
				barWidth += 20;
				double temp = barWidth*0.4;
				barWidthSide = (int) temp;
				double temp2 = barWidth*0.6;
				barRightX = (int) temp2;
			}			
		}
		if (powerUp.equals("narrower")) {
			
			if (barWidth > 60) {
				Sound.playClip("resources/sfx_sound_neutral4.wav");
				barWidth -= 20;
				double temp = barWidth*0.4;
				barWidthSide = (int) temp;
				double temp2 = barWidth*0.6;
				barRightX = (int) temp2;
			}	
		}
		if (powerUp.equals("faster")) {
			if (delay > 15) {
				Sound.playClip("resources/sfx_sound_neutral10.wav");
				delay -= 5;
				timer.stop();
				timer = new Timer(delay, this);
				timer.start();
			}
		}
		if (powerUp.equals("slower")) {
			if (delay < 25) {
				Sound.playClip("resources/sfx_sound_neutral10.wav");
				delay += 5;
				timer.stop();
				timer = new Timer(delay, this);
				timer.start();
			}
		}
	}
	
	public void powerUpCheck (String powerUp) {
		
		if (powerUp.equals("wider")) {
			if (barWidth < 140) {
				check = true;
			}
		}
		if (powerUp.equals("narrower")) {
			if (barWidth > 60) {
				check = true;
			}
		}
		if (powerUp.equals("faster")) {
			if (delay > 15) {
				check = true;
			}
		}
		if (powerUp.equals("slower")) {
			if (delay < 25) {
				check = true;
			}
		}
		
	}

	public void gameOver() {
		
		scoreCheck = false;
		scores = HighscoreList.getScores(score);
		HighscoreList.writeScores();
	}
	
	public void reset() {
		
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
		map = new MapGenerator(rows, cols);
		delay = 20;
		timer.stop();
		timer = new Timer(delay, this);
		timer.start();
		repaint();
	}
		
	@Override
	public void keyTyped(KeyEvent e) {}
}
