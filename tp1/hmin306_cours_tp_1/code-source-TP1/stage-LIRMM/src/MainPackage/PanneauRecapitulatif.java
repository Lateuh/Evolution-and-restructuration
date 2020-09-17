package MainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

//1 panneau dans onglets recapitulatifs
public class PanneauRecapitulatif extends Panneau{

	private String nomChamps="";
	
	private static final long serialVersionUID = 1L;

	//constructeurs
	public PanneauRecapitulatif(Color c) {
		super(c);
	}

	public PanneauRecapitulatif(Color c, String nomChamps) {
		super(c);
		this.nomChamps = nomChamps;
	}
	
	public PanneauRecapitulatif(String nomChamps) {
		super();
		this.nomChamps = nomChamps;
	}
	
	//accesseurs
	public String getNomChamps() {
		return nomChamps;
	}

	public void setNomChamps(String nomChamps) {
		this.nomChamps = nomChamps;
	}
	
	//autres methodes
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int h = getHeight();
	    int w = getWidth();
	    
	    int nbLines = 3;
	    int maxLetters = 9;
	    int size = min(h/nbLines, w/maxLetters);
	    int hgap = (h-(nbLines*size))/(nbLines+1);
	    int vgap = (w-maxLetters*size)/2;
	    int ligneReturn = size;
	    
		g.setColor(Color.WHITE);
		
		Font font = new Font(" TimesRoman ", 0,size);
		g.setFont(font);
		
		g.drawString("Consomation" ,vgap , (ligneReturn+hgap)*1);
		g.drawString("du "+nomChamps ,vgap , (ligneReturn+hgap)*2);
		g.drawString("Pendant la journee" ,vgap , (ligneReturn+hgap)*3);
	}

	private int min(int i, int j) {
		return (i<j?i:j);
	}
}











