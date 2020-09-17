package MainPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class Panneau extends JPanel {
	private Color backColor=Color.WHITE; //couleur de fond du panneau
	private static final long serialVersionUID = 1L; 
	
	//constructeur
	public Panneau() {
		super();
		this.setBackground(Color.WHITE);
		this.backColor=Color.WHITE;
	}
	public Panneau(Color c) {
		super();
		this.setBackground(c);
		this.backColor=c;
	}

	//getter et setter
	public Color getBackColor() {
		return backColor;
	}
	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
	
	//autres methodes
	//affiche component utiliser pr Swing
	public void paintComponent(Graphics g) {
		g.setColor(this.backColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}
