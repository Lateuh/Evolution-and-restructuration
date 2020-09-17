package MainPackage;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JTabbedPane;

public class Fenetre extends JFrame {
	
	private JTabbedPane onglet;
	private String[] nameOnglet = {"Temps reel","Recapitulatif","Configuration","Positionnement"};
	private Panneau[] tPan = { new Panneau(Color.WHITE), new Panneau(Color.WHITE), new Panneau(Color.WHITE), new Panneau(Color.WHITE) }; 
	private PanneauTempsReel PanneauConsoActuelle;
	private PanneauTempsReel PanneauConsoMoyJournee;
	private PanneauTempsReel PanneauConsoJournee;
	private PanneauTempsReel PanneauConsoJourPrecedent;
	
	private Graphique graph1;
	private Graphique graph2;
	private PanneauRecapitulatif caseTextGraph1;
	private PanneauRecapitulatif caseTextGraph2;
	
	private PanneauAjoutComposant panneauAjoutComposant;
	private PanneauSuppression panneauSuppression;
	private PanneauListeComposants panneauListeComposants;
	
	private ArrayList<Conso> graphData1;
	private ArrayList<Conso> graphData2;
	
	private static final long serialVersionUID = 1L;

	public Fenetre(ArrayList<Conso> tmpGraphData1, ArrayList<Conso> tmpGraphData2) throws NumberFormatException, ParseException, IOException{   
		
		//Couleurs
		Color caseVide    = new Color(129, 145, 166);
		Color base        = new Color(3, 34, 76);
		Color buttonColor = base;
	
		Color fondCourbe  = new Color(200,200,200);
		Color courbe      = new Color(0,0,255);
		Color aireCourbe  = new Color(200,200,255);
		Color fondEchelle = new Color(230,230,230);
		Color etiquette   = new Color(0,0,0);

		Graphique.setFondCourbe(fondCourbe);
		Graphique.setCourbe(courbe);
		Graphique.setAireCourbe(aireCourbe);
		Graphique.setFondEchelle(fondEchelle);
		Graphique.setEtiquette(etiquette);
		
		Button.setColor(buttonColor);
		
		//definition de la fenetre de base
		this.setLocationRelativeTo(null);
	    this.setTitle("my windows");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    //mise de la fenetre au milieu de l'ecran
	    Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int ScreenWidth  = (int)dimension.getWidth();
		int ScreenHeight = (int)dimension.getHeight();
		int height = 800;
		int width = (int) (height*1.618);
	    this.setSize(width, height);
	    this.setLocation(ScreenWidth/2 - width/2, ScreenHeight/2 - height/2);
	    
	    //creation de 3 onglets
	    onglet = new JTabbedPane();
		for (int i=0; i<4; i++) {
			onglet.add(nameOnglet[i], tPan[i]);
		}
		this.getContentPane().add(onglet);
		
		//Fenetre Temps Reel
	    //gridlayout et distance entre fenetre
		GridLayout gl = new GridLayout(2,2);
	    gl.setHgap(5);
	    gl.setVgap(5);
	     
	    //ajout de la grille et des differentes informations dans l'onglet "temps reel"
	    tPan[0].setLayout(gl);
	    PanneauConsoActuelle = new PanneauTempsReel(base);
	    PanneauConsoActuelle.setDisplayKey("Consommation en temps reel");
	    PanneauConsoMoyJournee = new PanneauTempsReel(base);
	    PanneauConsoMoyJournee.setDisplayKey("Consommation moyenne de la journee");
	    PanneauConsoJournee = new PanneauTempsReel(base);
	    PanneauConsoJournee.setDisplayKey("Consommation totale de la journee");
	    PanneauConsoJourPrecedent = new PanneauTempsReel(base);
	    PanneauConsoJourPrecedent.setDisplayKey("Consommation totale de la veille");
	    
	    //addd les panneaux
		tPan[0].add(PanneauConsoActuelle);
		tPan[0].add(PanneauConsoMoyJournee);
		tPan[0].add(PanneauConsoJournee);
		tPan[0].add(PanneauConsoJourPrecedent);
		
		//Fenetre recapitulatif
		GridLayout glRecap;
		
		if (tmpGraphData2==null) 
		{
			glRecap = new GridLayout(1,1);
			glRecap.setHgap(5);
			glRecap.setVgap(5);
			
			String courbe1= "total";
			this.graphData1 = tmpGraphData1;
			
			double gap = 0.001;
			
			tPan[1].setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			
			caseTextGraph1 = new PanneauRecapitulatif(base, courbe1);
			graph1 = new Graphique(0,caseTextGraph1);
			
			gbc.weightx = 0.75;
			gbc.weighty = 0.5-gap/2;
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			
			gbc.fill = GridBagConstraints.BOTH;
			tPan[1].add(graph1, gbc);
			
			gbc.weightx = 0.25;
			gbc.weighty = 0.5-gap/2;
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			
			tPan[1].add(caseTextGraph1, gbc);
			
			gbc.weightx = 0.75;
			gbc.weighty = 0.5-gap/2;
			
			
			gbc.gridx = 0;
			gbc.gridy = 2;

			graph2=null;
		}
		else
		{
			String courbe1= "CPU";
	        String courbe2= "GPU";
			
			this.graphData1 = tmpGraphData1;
			this.graphData2 = tmpGraphData2;
			
			//initialisation graphiques
			double gap = 0.001;
			
			tPan[1].setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			
			caseTextGraph1 = new PanneauRecapitulatif(base, courbe1);
			caseTextGraph2 = new PanneauRecapitulatif(base, courbe2);
			graph1 = new Graphique(1,caseTextGraph1);
			graph2 = new Graphique(0,caseTextGraph2);
			
			gbc.weightx = 0.75;
			gbc.weighty = 0.5-gap/2;
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			
			gbc.fill = GridBagConstraints.BOTH;
			tPan[1].add(graph1, gbc);
			
			gbc.weightx = 0.25;
			gbc.weighty = 0.5-gap/2;
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			
			tPan[1].add(caseTextGraph1, gbc);
			
			gbc.weightx = 0.75;
			gbc.weighty = 0.5-gap/2;
			
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			
			tPan[1].add(graph2, gbc);
			
			gbc.weightx = 0.25;
			gbc.weighty = 0.5-gap/2;
			
			gbc.gridx = 1;
			gbc.gridy = 2;
			
			tPan[1].add(caseTextGraph2, gbc);
			
			gbc.weightx = 1;
			gbc.weighty = gap;
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			
			tPan[1].add(new Panneau(Color.WHITE),gbc);
		}
		
		panneauListeComposants = new PanneauListeComposants(base);
		panneauSuppression = new PanneauSuppression(base, panneauListeComposants);
		panneauAjoutComposant = new PanneauAjoutComposant(base, panneauSuppression, panneauListeComposants);
		
		//Fenetre Plus de precision
		double gap = 0.001;
		tPan[2].setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		//panel liste des composants
		gbc.gridwidth = 3;
		gbc.weightx = 1;
		gbc.weighty = 0.75-gap/2;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		gbc.fill = GridBagConstraints.BOTH;
		tPan[2].add(panneauListeComposants, gbc);
		
		//Gap
		gbc.gridwidth = 3;
		gbc.weightx = 1;
		gbc.weighty = gap;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		
		tPan[2].add(new Panneau(Color.WHITE),gbc);
		
		//panel Ajout
		gbc.gridwidth = 1;
		gbc.weightx = 0.5-gap/2;
		gbc.weighty = 0.25-gap/2;
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		gbc.fill = GridBagConstraints.BOTH;
		tPan[2].add(panneauAjoutComposant, gbc);
		
		//Gap
		gbc.gridwidth = 1;
		gbc.weightx = gap;
		gbc.weighty = 0.25-gap/2;
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		
		tPan[2].add(new Panneau(Color.WHITE),gbc);
		
		//panel Suppression
		gbc.gridwidth = 1;
		gbc.weightx = 0.5-gap/2;
		gbc.weighty = 0.25-gap/2;
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		
		gbc.fill = GridBagConstraints.BOTH;
		tPan[2].add(panneauSuppression, gbc);
		
		
		
		//Fenetre Positionnement
		GridLayout glPositionnement = new GridLayout(3,3);
		glPositionnement.setHgap(2);
		glPositionnement.setVgap(2); 
		tPan[3].setLayout(glPositionnement);
		tPan[3].add(new ButtonHautGauche("Position en haut a gauche", this));
		tPan[3].add(new Panneau(caseVide));
		tPan[3].add(new ButtonHautDroit("Position en haut a droite", this));
		tPan[3].add(new Panneau(caseVide));
		tPan[3].add(new ButtonMilieu("Position au milieu", this));
		tPan[3].add(new Panneau(caseVide));
		tPan[3].add(new ButtonBasGauche("Position en bas a gauche", this));
		tPan[3].add(new Panneau(caseVide));
		tPan[3].add(new ButtonBasDroit("Position en bas a droite", this));
		
	
	    this.setVisible(true);
	  }
	
	//accesseurs aux apnneaux de temps reel
	public PanneauTempsReel getPanneauActuelle() {
		return PanneauConsoActuelle;
	}
	
	public PanneauTempsReel getPanneauConsoMoyJournee() {
		return PanneauConsoMoyJournee;
	}
	
	public PanneauTempsReel getPanneauConsoJournee() {
		return PanneauConsoJournee;
	}
	
	public PanneauTempsReel getPanneauConsoJourPrecedent() {
		return PanneauConsoJourPrecedent;
	}
	
	public PanneauAjoutComposant getPanneauAjoutComposant() {
		return panneauAjoutComposant;
	}
	
	public PanneauSuppression getPanneauSuppression() {
		return panneauSuppression;
	}
	
	public PanneauListeComposants getPanneauListeComposants() {
		return panneauListeComposants;
	}
	
	public void updateGraphiques() {
		graph1.updateGraphique();
		if (graph2!=null)
			graph2.updateGraphique();
	}
	
	public void setGraphInfo(ArrayList<Conso> data1,ArrayList<Conso> data2) {
		this.graphData1 = data1;
		this.graphData2 = data2;
	}
}