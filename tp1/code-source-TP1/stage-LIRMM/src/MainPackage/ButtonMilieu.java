package MainPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ButtonMilieu extends Button implements MouseListener  {
	
	//attributs
	private String name;
	private JFrame frame;
	
	//constructeur
	public ButtonMilieu(String str, JFrame frame) {
		super(str);
		this.name = str;
		this.frame = frame;
		this.addMouseListener(this);
	}
	
	//place fenetre millieu et modifiable
	public void mouseClicked(MouseEvent arg0) {
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int ScreenWidth  = (int)dimension.getWidth();
		int ScreenHeight = (int)dimension.getHeight();
		int height = 800;
		int width = (int) (height*1.618);
	    frame.setSize(width, height);
	    frame.setLocation(ScreenWidth/2 - width/2, ScreenHeight/2 - height/2);
	    frame.setAlwaysOnTop(false);
		frame.setResizable(true);
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public void paintComponent(Graphics g){
		g.setColor(getColor());
	    g.fillRect(0, 0, this.getWidth(), this.getHeight());
	    g.setColor(Color.WHITE);
	    //changer taille de la police
	    int max=(this.getWidth()>this.getHeight()?this.getWidth():this.getHeight());
	    Font font = new Font(" TimesRoman ", 0, max/15);
		g.setFont(font);
	    g.drawString(this.name, this.getWidth() / 4, this.getHeight() / 2);
	 }
}