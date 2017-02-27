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
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements ActionListener, KeyListener {

	public int score = 0;
	private boolean play = false;
	private boolean scoreCheck = true;
	private int rows = 5;
	private int cols = 8;
	private int totalBricks = rows * cols;
	private Timer timer;
	private int delay = 8;
	private int playerX = 310;
	private int ballposX = 240;
	private int ballposY = 220;
	private int ballXdir = 1;
	private int ballYdir = 2;
	private int[] scores;
	private String[] placings = {"","10:","09:","08:","07:","06:","05:","04:","03:","02:","01:"};
	private MapGenerator map;
	private HighscoreList hsl;

	public Gameplay() {
		map = new MapGenerator(rows, cols);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
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
		g.fillRect(playerX, 550, 100, 8);

		// ball
		g.setColor(Color.WHITE);
		g.fillOval(ballposX, ballposY, 20, 20);

		if (totalBricks <= 0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.GREEN);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
			g.drawString("You Won!", 270, 300);

			g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}

		if (ballposY > 570) {
			
			if (scoreCheck) {
				scoreCheck = false;
				hsl = new HighscoreList();
				scores = hsl.getScores(score);
				hsl.writeScores();		
			}
			
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
			g.setColor(Color.RED);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
			g.drawString("Game Over", 265, 300);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 240, 350);
		}

		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (play) {

			Rectangle barRight = new Rectangle(playerX + 55, 550, 45, 8);
			Rectangle barLeft = new Rectangle(playerX, 550, 45, 8);
			Rectangle bar = new Rectangle(playerX, 550, 100, 8);
			Rectangle ball = new Rectangle(ballposX, ballposY, 20, 20);

			if (ball.intersects(bar)) {

				if (ball.intersects(barLeft)) {
					if (ballXdir < 0 && ballXdir > -3) {
						ballXdir--;
					}
					if (ballXdir > 1) {
						ballXdir--;
					}
				}

				if (ball.intersects(barRight)) {
					if (ballXdir < -1) {
						ballXdir++;
					}
					if (ballXdir > 0 && ballXdir < 3) {
						ballXdir++;
					}
				}

				if (ballXdir == -3 || ballXdir == 3) {
					ballYdir = 1;
				} else {
					ballYdir = 2;
				}

				ballYdir = -ballYdir;
			}

			A: for (int i = 0; i < map.map.length; i++) {
				for (int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

						if (ball.intersects(brickRect)) {
							map.setBrickValue(i, j);
							if (map.getBrickValue(i, j) == 0) {
								totalBricks--;
								score += 2520 / (i + 1);
							}
							score += 500;

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
				ballXdir = -ballXdir;
			}
			if (ballposY < 0) {
				ballYdir = -ballYdir;
			}
			if (ballposX > 670) {
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (playerX >= 600) {
				playerX = 600;
			} else {
				moveRight();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (playerX < 10) {
				playerX = 10;
			} else {
				moveLeft();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {
				play = true;
				scoreCheck = true;
				ballposX = 240;
				ballposY = 220;
				ballXdir = 1;
				ballYdir = 2;
				playerX = 310;
				score = 0;
				totalBricks = rows * cols;
				map = new MapGenerator(rows, cols);
				repaint();
			}
		}
	}

	public void moveRight() {
		play = true;
		playerX += 15;
	}

	public void moveLeft() {
		play = true;
		playerX -= 15;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
