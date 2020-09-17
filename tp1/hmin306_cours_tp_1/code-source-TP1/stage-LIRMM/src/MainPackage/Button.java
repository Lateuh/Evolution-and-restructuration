package MainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;

public class Button extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private static Color color = new Color(3, 34, 76);
	
	public Button(String str) {
		super(str);
		this.name = str;

	}
	//utiliser par swing pour l'affichage
	public void paintComponent(Graphics g){
		g.setColor(color);
	    g.fillRect(0, 0, this.getWidth(), this.getHeight());
	    g.setColor(Color.WHITE);
	    //changer taille de la police
	    int max=(this.getWidth()>this.getHeight()?this.getWidth():this.getHeight());
	    Font font = new Font(" TimesRoman ", 0, max/15);
		g.setFont(font);
	    g.drawString(this.name, this.getWidth() / 7 , this.getHeight() / 2);
	 }
	
	//retourner la couleur du boutton
	public static Color getColor() {
		return color;
	}

	//changer la couleur du boutton
	public static void setColor(Color color) {
		Button.color = color;
	}
}
