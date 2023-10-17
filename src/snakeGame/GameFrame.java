package snakeGame;

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {
	
	public GameFrame() {
		
		ImageIcon snakeIcon = new ImageIcon("realSnake.jpg");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(1200,100);
		this.setTitle("SNAKE EATER");
		//this.setIconImage(snakeIcon.getImage());
		this.add(new GamePanel());
		this.setVisible(true);
		this.pack(); // optional but messes it up
	}

}
