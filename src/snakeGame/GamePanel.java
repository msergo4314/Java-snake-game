package snakeGame;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener{

	/**
	 * @author Martin sergo - but not actually, I took it from youtube mostly
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Color myRandColour = new Color(0, 200, 50);
	private int pelletColour = 0;
	
	private final int LENGTH = 650, WIDTH = 550; // dimensions of the frame
	private final int UNIT_SIZE = 20; // surface area of each unit
	private final int GAME_UNITS = (WIDTH * LENGTH) / UNIT_SIZE; // number of units is total surface area / unit size
	private final int DELAY = 1; // delay (in ms)
	// frame rate = (delay e-3)^-1 >= 60
	
	final int X[] = new int[GAME_UNITS];
	final int Y[] = new int[GAME_UNITS];
	
	int bodyParts = 5;
	private int timerCount = 0;
	
	int foodEaten = 0; // counter for food pellets eaten
	int foodX ,foodY;
	char direction = 'R'; // snake begins going to the right
	boolean running = false; // not quite yet...
	Timer timer;
	Random random;
	ImageIcon snakeIcon = new ImageIcon("realSnake.jpg");	
	
	public GamePanel() {
		for (int i = 0; i < bodyParts; i++) {
			Y[i] = (WIDTH / 2 / UNIT_SIZE) * UNIT_SIZE;
		}
		random = new Random();
		this.setPreferredSize(new Dimension(LENGTH, WIDTH));
		this.setLayout(null);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame(); // begin the game!!!
		this.setBackground(Color.BLACK);	
		this.setOpaque(true);
		this.setVisible(true);
	}
	
	public void startGame() {
		newFood();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if (running) {
// 			for(int i=0; i < WIDTH / UNIT_SIZE; i++) {
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, WIDTH);
//			}
//			for(int i=0; i < LENGTH / UNIT_SIZE; i++) {
//				g.drawLine(0, i*UNIT_SIZE, LENGTH, i*UNIT_SIZE);
//			}
			g.drawImage(snakeIcon.getImage(), 0, 0, null);
						
			switch (pelletColour) {
		    case 0:
		        g.setColor(Color.red);
		        break;
		    case 1:
		        g.setColor(Color.blue);
		        break;
		    default:
		        // Handle unexpected values
		        g.setColor(Color.black); // Set a default color or handle the situation appropriately
		        break;
		}

			g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE); // draw the food
			
			for (int i = 0; i < bodyParts; i++) {
				if (i==0) {
					g.setColor(new Color(0, 100, 80));
					g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(myRandColour);
					//g.setColor(new Color(45, 180, 0)); // can also randomize colors
					g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
				}
				
			}
			g.setColor(new Color(100, 100, 100));
			g.setFont(new Font("Adobe Garamond", Font.PLAIN, 25));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score Count: "+ foodEaten, (LENGTH - metrics.stringWidth("Score Count: " + foodEaten))/2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}
	public void newFood() {
		foodX = random.nextInt((int)(LENGTH/ UNIT_SIZE)) * UNIT_SIZE;
		foodY = random.nextInt((int)(WIDTH/ UNIT_SIZE)) * UNIT_SIZE;
	}
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			X[i] = X[i-1]; // move old segments to where previous segments were
			Y[i] = Y[i-1];
		}
		
		switch (direction) { // move the head
		case 'U':
			Y[0] -= UNIT_SIZE;
			break;
		case 'D':
			Y[0] += UNIT_SIZE;
			break;
		case 'L':
			X[0] -= UNIT_SIZE;
			break;
		case 'R':
			X[0] += UNIT_SIZE;
			break;
		}
		
	}
	public void checkFood(){
		if ((X[0] == foodX) && Y[0] == foodY) {
			bodyParts++;
			foodEaten++;
			myRandColour = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
			pelletColour = random.nextInt(2);
			newFood();
		}
	}
	public void checkCollisions() {
		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((X[0] == X[i]) && Y[0] == Y[i]) {
				running = false; // end of game
			}
		}
		// check if head touches left border
		if (X[0] < 0) {
			//X[0] = WIDTH;
			running = false;
		}
		// check if head touches right border
		if (X[0] > LENGTH) {
			//X[0] = 0;
			running = false;
		}
		// check if head touches top border
		if (Y[0] < 0) {
			//Y[0] = WIDTH;
			running = false;
		}
		// check if head touches bottom border
		if (Y[0] > WIDTH) {
			//Y[0] = 0;
			running = false;
		}
		if (!running) {
			timer.stop();
		}
				
	}
	public void gameOver(Graphics g) {
		g.setColor(new Color(135, 20, 20));
		g.setFont(new Font("Adobe Garamond", Font.PLAIN, 45)); // ds style baby
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("YOU DIED", (int)(LENGTH - metrics1.stringWidth("YOU DIED"))/2, (int)(WIDTH/2 - 130));

		g.setColor(new Color(100, 100, 100));
		g.setFont(new Font("OptimusPrincepsSemiBold", Font.PLAIN, 25));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score Count: "+ foodEaten, (int)(LENGTH - metrics2.stringWidth("Score Count: "+foodEaten))/2,(int) (WIDTH/2 - 120 + g.getFont().getSize()));
		
		JButton restartButton = new JButton("Restart");
		restartButton.setFocusable(false);
		restartButton.setBounds((int)(LENGTH - metrics2.stringWidth("Score Count: "+foodEaten))/2
				, (int) (WIDTH/2 - 80 + g.getFont().getSize()), 150, 50);
		restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
		restartButton.setBorder(BorderFactory.createLineBorder(myRandColour, 7));
		restartButton.setPreferredSize(new Dimension(150, 50));
		restartButton.setBackground(Color.LIGHT_GRAY);
		restartButton.addActionListener(e -> {
			new GameFrame();
		});
		this.add(restartButton);
	}	
	public class MyKeyAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			checkArrows(e);
			checkWASD(e);
		}
		
		public void checkArrows(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
		
		public void checkWASD(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_D:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_W:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_S:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

	
	@Override
		public void actionPerformed(ActionEvent e) {
		timerCount += 1; // increment timer count
			if (running) {
				if (timerCount % 7 == 0) {
					move();
					checkCollisions();
					timerCount = 0;
				}
				checkFood();
			}
			repaint();
		}

}
