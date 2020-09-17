package MainPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

//panneau pour ajout de composant dans onglet configuration
public class PanneauAjoutComposant extends Panneau{
	
	//elements visuels du panneau
	private JLabel nom = new JLabel("Nom composant");
	private JTextField nomComposant = new JTextField();
	
	private JLabel conso = new JLabel("Consommation composant");
	private JTextField consoComposant = new JTextField();
	
	private JLabel textTypeComposant = new JLabel("Type composant");
	private JComboBox typeComposant;
	
	private JLabel textConstructeur = new JLabel("Constructeur");
	private JTextField constructeur = new JTextField();
	
	private JLabel textMiseEnCirculation = new JLabel("Mise en circulation");
	private JComboBox MiseEnCirculation;
	
	private JButton ajouter;
	
	//autres attributs
	private Hashtable<String, ArrayList<String>> table;
	private PanneauSuppression panSup;
	private PanneauListeComposants panCompo;
	
	//constructeur
	public PanneauAjoutComposant(Color c, PanneauSuppression panSup, PanneauListeComposants panCompo) throws NumberFormatException, IOException {
		super(c);
		
		this.panSup = panSup;
		this.panCompo = panCompo;
		
		table =  Controller.readConfigFile();
		
		//init les JTextFields
		nomComposant.addKeyListener(new ClavierListener());
		consoComposant.addKeyListener(new ClavierListener());
		constructeur.addKeyListener(new ClavierListener());
		
		int sizeTypes = EnumTypeComposant.values().length;
		String[] types = new String[sizeTypes];
		for(int i = 0; i < sizeTypes; i++) {
			types[i] = String.valueOf(EnumTypeComposant.values()[i]);
		}
		typeComposant = new JComboBox(types);
		
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		int size = curYear - 1990 + 1;
		String[] annee = new String[size];
		for(int i = 0; i < size; i++) {
			annee[i] = String.valueOf(1990 + i);
		}
		MiseEnCirculation = new JComboBox(annee);
		
		ajouter = new ButtonAjout ("Ajouter", nomComposant, consoComposant, constructeur, typeComposant, MiseEnCirculation, table, panSup, panCompo);

		this.add(nom);
	    this.add(nomComposant);
	    this.add(conso);
	    this.add(consoComposant);
	    this.add(textTypeComposant);
	    this.add(typeComposant);
	    this.add(textConstructeur);
	    this.add(constructeur);
	    this.add(textMiseEnCirculation);
	    this.add(MiseEnCirculation);
	    this.add(ajouter);
	}
	
	//affichage du panneau
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		//couleur
		g.setColor(Color.WHITE);
		nom.setForeground(Color.WHITE);
		conso.setForeground(Color.WHITE);
		textTypeComposant.setForeground(Color.WHITE);
		textConstructeur.setForeground(Color.WHITE);
		textMiseEnCirculation.setForeground(Color.WHITE);
		
		//string Ajout
		String ajout = "AJOUT";
		int fontSize = width/37;
		Font font = new Font("Arial", Font.BOLD, fontSize);
		g.setFont(font);
		int xAjout =  4 * ajout.length() / 6;
		int yAjout = 4 * ajout.length();
		g.drawString(ajout, xAjout, yAjout);
		
		//mise d'une font sur les textes
		Font fontText = new Font("Arial", Font.BOLD, 4 * fontSize/7);
		nom.setFont(fontText);
		conso.setFont(fontText);
		textTypeComposant.setFont(fontText);
		textConstructeur.setFont(fontText);
		textMiseEnCirculation.setFont(fontText);
		
		//placement text + zone ecriture
		int widthNom = xAjout + width/25;
		int heightNom = yAjout + height/7;
		
		nom.setLocation(widthNom, heightNom);
		nomComposant.setPreferredSize(new Dimension(5 * fontSize, fontSize));
		nomComposant.setLocation(widthNom + width / 8, heightNom);
		
		int widthConso = widthNom;
		int heightConso = heightNom + height/4;
		
		conso.setLocation(widthConso, heightConso);
		consoComposant.setPreferredSize(new Dimension(5 * fontSize, fontSize));
		consoComposant.setLocation(widthConso + width / 5, heightConso);
		
		int widthConstructeur = widthNom;
		int heightConstructeur = heightNom + height/2;
		
		textConstructeur.setLocation(widthConstructeur, heightConstructeur);
		constructeur.setPreferredSize(new Dimension(5 * fontSize, fontSize));
		constructeur.setLocation(widthConstructeur + width / 9, heightConstructeur);
		
		int widthTypeComposant = widthConso + 2 * width / 5;
		int heightTypeComposant = heightNom;
		
		textTypeComposant.setLocation(widthTypeComposant, heightTypeComposant);
		typeComposant.setPreferredSize(new Dimension(5 * fontSize, fontSize));
		typeComposant.setLocation(widthTypeComposant + width / 8, heightTypeComposant);
		
		int widthMiseEnCirculation = widthTypeComposant;
		int heightMiseEnCirculation = heightTypeComposant + height/4;
		
		textMiseEnCirculation.setLocation(widthMiseEnCirculation, heightMiseEnCirculation);
		MiseEnCirculation.setPreferredSize(new Dimension(5 * fontSize, fontSize));
		MiseEnCirculation.setLocation(widthMiseEnCirculation + width / 7, heightMiseEnCirculation);
		
		ajouter.setLocation(widthTypeComposant, heightConso + height/4);
		
		this.updateUI();
	}
}
