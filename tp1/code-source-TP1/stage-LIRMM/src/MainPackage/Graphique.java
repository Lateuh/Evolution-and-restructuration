package MainPackage;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.JPanel;

public class Graphique extends JPanel implements MouseListener {
	private static Hashtable<String, ArrayList<Conso>> allTheData;
	private ArrayList<String> composName;
	private int index;
	private ArrayList<Conso> data;
	
	PanneauRecapitulatif monPanneau;
	
	private double minConso;
	private double maxConso;
	private double actuConso;
	private static Color fondCourbe = new Color(200,200,200);
	private static Color courbe = Color.blue;
	private static Color aireCourbe = new Color(200,200,255);
	private static Color fondEchelle = new Color(230,230,230);
	private static Color etiquette = new Color(0,0,0);
	
	private boolean isMousePressed = false;
	private int mouseX = 0;
	private int mouseY = 0;
	
	private ClicDroitGraph clickDetector;
	
	public Graphique(int index,PanneauRecapitulatif monPanneau) {
		super();
		this.monPanneau= monPanneau; 
		this.index = index;
		composName = new ArrayList<String>(allTheData.keySet());
		this.data = allTheData.get(composName.get(index));
		this.addMouseListener(this);
		
		clickDetector = new ClicDroitGraph(this, this.composName);
		this.addMouseListener(clickDetector);
		setIndex(index);
	}
	
	public void paintComponent(Graphics g){
		this.data = allTheData.get(composName.get(index));
		this.updateData();
		tracageDeLaCourbe(g); 
	}
	
	private void tracageDeLaCourbe(Graphics g) {
	
		int h = getHeight();
	    int w = getWidth();
	    int xStart = 55;
	    int yStart = 0;
	    int xEnd = w;
	    int yEnd = h;
	    
	    int fontSize=14;
	    
	    boolean doSmooth = false;
	    int smooth = 8;
	    int nbPoint = data.size();
	    
	    int maxTagH = fontSize;
	    int minTagH = h;
	    int currentTagH =  map(actuConso,0,maxConso,h,0);
	    currentTagH = (currentTagH < maxTagH + fontSize ? maxTagH + fontSize : currentTagH);
	    currentTagH = (currentTagH > minTagH - fontSize ? minTagH - fontSize : currentTagH);
	    
	    
	    g.setColor(fondCourbe);
	    g.fillRect(xStart, yStart, xEnd, yEnd);
	    
	    g.setColor(fondEchelle);
	    g.fillRect(0, 0, xStart, h);
	    
	    
	    g.setColor(aireCourbe);
	    int xValues[] = new int[nbPoint+2];
	    int yValues[] = new int[nbPoint+2];
	    
	    for(int i  = 0 ; i < nbPoint ; i++) {
	    	double val = 0;
	    	
	    	if(doSmooth) {
		    	int startInd = i-smooth/2;
		    	int endInd = i+smooth/2;
		
		    	
		    	if(nbPoint<=smooth) {
		    		startInd = 0;
		    		endInd = 0;
		    	} else if(startInd<0) {
		    		//endInd+=0-startInd;
		    		startInd = 0;
		    	} else if(endInd>=nbPoint) {
		    		//startInd -= endInd-nbPoint-1;
		    		endInd = nbPoint-1;
		    	}

		    	for (int j = startInd ; j< endInd ; j++) {
		    		val += data.get(j).getConso() / (endInd-startInd);
		    	}
	    	} else {
	    		val = data.get(i).getConso();
	    	}
	    	xValues[i] = map(i,0,nbPoint-1,xStart,xEnd);
	    	yValues[i] = map(val,minConso,maxConso,yEnd,yStart);
	    }
	    xValues[nbPoint] = xEnd;
	    yValues[nbPoint] = yEnd;
	    
	    xValues[nbPoint+1] = xStart;
	    yValues[nbPoint+1] = yEnd;
	    
	    Polygon polygon1 = new Polygon( xValues, yValues, nbPoint+2);
	    g.fillPolygon( polygon1 );
	    
	    g.setColor(courbe);
	    g.drawPolygon( polygon1 );
	    
	    //////échelle
	    g.setColor(etiquette);
		Font font = new Font(" TimesRoman ", 20, fontSize);
		g.setFont(font);
		g.drawString(String.valueOf((int)(maxConso*10)/10.0)+" W", 0, fontSize);
		
		g.setColor(etiquette);
		g.setFont(font);
		g.drawString(String.valueOf((int)(actuConso*10)/10.0)+" W", 0, currentTagH);
		
		g.setColor(etiquette);
		g.setFont(font);
		g.drawString(String.valueOf((int)(0*10)/10.0)+" W", 0, h);
		
		if(isMousePressed) {
			int calculatedIndex = max(0,map(mouseX,xStart,w,0,data.size()));
			String val = String.valueOf((int) (10 * data.get(calculatedIndex).getConso()) / 10.0) + " W";
			drawInfoRect(g,val,map(calculatedIndex,0,data.size()-1,xStart,w) , min((int)(h-fontSize*1.618),map(data.get(calculatedIndex).getConso(),minConso,maxConso,yEnd,yStart)),63,(int) (fontSize*2));
		}
	}
	  private int max(int i, int j) {
		
		return (i>j ? i : j);
	}

	private int min(int i, int y) {
		return (i<y ? i : y);
	}

	//getMousePosition();
	public void updateData() {
		this.minConso = 0;
		this.maxConso = this.data.get(0).getConso();
		this.actuConso = data.get(data.size()-1).getConso();
		
		for(int i = 0 ; i<data.size() ; i++) {
	    	if(this.data.get(i).getConso()>this.maxConso)
	    		this.maxConso = this.data.get(i).getConso();
	    }
	}
	
	public void updateGraphique() {
		updateData();
		super.updateUI();
	}
	
	static public final int map(float value, float istart, float istop, float ostart, float ostop) {
		return (int) ( (double)ostart + ((double)ostop - (double)ostart) * (((double)value - (double)istart) / ((double)istop - (double)istart)));
	}
	
	
	static public final int map(double value, double istart, double istop, double ostart, double ostop) {	
		return (int)((double)ostart + ((double)ostop - (double)ostart) * (((double)value - (double)istart) / ((double)istop - (double)istart)));
	}

	public static Color getFondCourbe() {
		return fondCourbe;
	}

	public static void setFondCourbe(Color fondCourbe) {
		Graphique.fondCourbe = fondCourbe;
	}

	public static Color getCourbe() {
		return courbe;
	}

	public static void setCourbe(Color courbe) {
		Graphique.courbe = courbe;
	}

	public static Color getAireCourbe() {
		return aireCourbe;
	}

	public static void setAireCourbe(Color aireCourbe) {
		Graphique.aireCourbe = aireCourbe;
	}

	public static Color getFondEchelle() {
		return fondEchelle;
	}

	public static void setFondEchelle(Color fondEchelle) {
		Graphique.fondEchelle = fondEchelle;
	}

	public static Color getEtiquette() {
		return etiquette;
	}

	public static void setEtiquette(Color etiquette) {
		Graphique.etiquette = etiquette;
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			isMousePressed = true;
			mouseX = getMousePosition().x;
			mouseY = getMousePosition().y;
			updateUI();
		}
	}

	public void mouseReleased(MouseEvent e) {
		isMousePressed = false;
		updateUI();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
	private void drawInfoRect(Graphics g, String valeur, int posx, int posy, int sizex, int sizey) {
        g.setColor(Color.WHITE);
        g.fillRect(posx, posy, sizex, sizey);
        
        g.setColor(Color.BLACK);
        int sizeValeur = (int) (sizey*0.6);
        Font font = new Font(Font.MONOSPACED, Font.BOLD, sizeValeur);
        g.setFont(font);
        g.drawString(valeur, posx, posy + sizeValeur);
    }

	public static void setAllTheData(Hashtable<String, ArrayList<Conso>> allTheData) {
		Graphique.allTheData = allTheData;
	}

	public void setIndex(int index) {
		this.index = index;
		monPanneau.setNomChamps(this.composName.get(index).toUpperCase());
		monPanneau.updateUI();
		updateUI();
	}
}
