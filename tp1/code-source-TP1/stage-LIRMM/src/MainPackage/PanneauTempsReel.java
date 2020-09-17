package MainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;


//panneau tempsReel dans onglet temps reel
public class PanneauTempsReel extends Panneau {
	private String nomChamps="";
	private String valeurChamp="";
	private static Hashtable<String, String> infosToDisplay;
	private ArrayList<String> keyList;
	private String displayKey = "";
	private PopClickListener clickDetector;
	private static final long serialVersionUID = 1L;

	//constructeurs
	public PanneauTempsReel(Color c) {
		super(c);
		this.keyList = new ArrayList<String>(infosToDisplay.keySet());
		Collections.sort(this.keyList);
		displayKey = this.keyList.get(0);
		clickDetector = new PopClickListener(this, this.keyList);
		this.addMouseListener(clickDetector);	
	}

	public PanneauTempsReel(Color c, String nomChamps, String valeurChamp) {
		super(c);
		this.nomChamps = nomChamps;
		this.valeurChamp = valeurChamp;
	}
	
	public PanneauTempsReel(String nomChamps, String valeurChamp) {
		super();
		this.nomChamps = nomChamps;
		this.valeurChamp = valeurChamp;
	}
	
	//accesseurs
	public String getNomChamps() {
		return nomChamps;
	}

	public void setNomChamps(String nomChamps) {
		this.nomChamps = nomChamps;
	}

	public String getValeurChamp() {
		return valeurChamp;
	}

	public void setValeurChamp(String valeurChamp) {
		this.valeurChamp = valeurChamp;
	}

	//autres methodes
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateChamps();
		
		g.setColor(Color.WHITE);
		
		/*
		//ajout du texte de champs
		int max=(this.getWidth()>this.getHeight()?this.getWidth():this.getHeight());
		Font font = new Font(" TimesRoman ", 0, max/20);
		g.setFont(font);
		g.drawString(nomChamps, max/10, max/10);
		
		//ajout du text de valeur du champs
		font = new Font("Courier", Font.BOLD, max/8);
		g.setFont(font);
		g.drawString(valeurChamp, this.getWidth()/4, this.getHeight()/2);
		*/
		
		int maxLength = keyList.get(0).length();
		for (int i=0; i < keyList.size(); i++) {
			if (keyList.get(i).length() > maxLength) {
				maxLength = keyList.get(i).length();
			}
		}
		
		int h = getHeight();
	    int w = getWidth();
	    
	    int nbLines = 2; 
	    int nbLetters = nomChamps.length();
	    int maxTextSize = (int) ((maxLength+3) *(1/1.61803398875));
	    int size = min(h/nbLines, w/maxTextSize);
	    int hgap = 0;
	    double monospacedWidth = 1/(1.61803398875); 
	    int vgap = (int) ((w-nbLetters*size*monospacedWidth)/2);
	   
	    int nbLines2 = 2;
	    int nbLetters2 = valeurChamp.length();
	    int textLength = (int) (nbLetters2*monospacedWidth) ;
	    int size2 = min(min(h/nbLines2, (int) ((w/nbLetters2)/monospacedWidth)),100);
	    int modifiedSize2 = (int) (size2*0.60);
	    int hgap2 = size +hgap + (h-size-hgap)/2 +modifiedSize2/2;
	    int vgap2 = (int) ((w-nbLetters2*size2*monospacedWidth)/2);
	    
		g.setColor(Color.WHITE);
		Font font = new Font(Font.MONOSPACED , Font.BOLD, size);

		g.setFont(font);
		g.drawString(nomChamps ,vgap , size+hgap);

		font = new Font(Font.MONOSPACED , Font.BOLD, size2);
		g.setFont(font);
		g.drawString(valeurChamp ,vgap2 , hgap2);
	}
	
	private void updateChamps() {
		nomChamps = displayKey;
		valeurChamp = infosToDisplay.get(displayKey);
		
		keyList = new ArrayList<String>(infosToDisplay.keySet());
		Collections.sort(keyList);
		clickDetector.setListe(keyList);
		
		
	}

	private int min(int i, int j) {
		return (i<j?i:j);
	}

	public static void setInfosToDisplay(Hashtable<String, String> infosToDisplay) {
		PanneauTempsReel.infosToDisplay = infosToDisplay;
	}

	public void setDisplayKey(String s) {
		if(keyList.contains(s))
			this.displayKey = s;
		else
			this.displayKey = keyList.get(0);
		updateUI();
	}
}
