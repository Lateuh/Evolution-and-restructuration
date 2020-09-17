package MainPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JLabel;

//panneau suppressions dans onglets configurations
public class PanneauSuppression extends Panneau  {
		//attributs
		private JLabel composant = new JLabel("Composant � supprimer");
		private JComboBox combo;
		private Hashtable<String, ArrayList<String>> table;
		private ButtonSuppression supprimer;
		private PanneauListeComposants panCompo;
		
		//constructeur
		public PanneauSuppression(Color c, PanneauListeComposants panCompo) throws NumberFormatException, IOException {
			super(c);
			
			this.panCompo = panCompo;
		
			table = Controller.readConfigFile();
			String[] tab = new String[table.size()];
			tab = getKeys();
			combo = new JComboBox<String>(tab);
			
			supprimer = new ButtonSuppression ("Supprimer", combo, table, panCompo);
			//Ajout du listener
			combo.addActionListener(new ItemAction(combo));

			this.add(composant);
		    this.add(combo);
		    this.add(supprimer);
		}
		
		//affichage du panneau
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int width = this.getWidth();
			int height = this.getHeight();
			
			//couleur
			g.setColor(Color.WHITE);
			composant.setForeground(Color.WHITE);
			
			//String suppression
			String suppression = "SUPPRESSION";
			int fontSize = width/20;
			Font font = new Font("Arial", Font.BOLD, fontSize);
			g.setFont(font);
			int xSuppression =  3 * suppression.length() / 5;
			int ySuppression = 7 * suppression.length() / 4;
			g.drawString(suppression, xSuppression, ySuppression);
			
			//placement label + combo
			int widthComposant = xSuppression + width/7;
			int heightComposant = ySuppression + height/4;
			
			Font fontText = new Font("Arial", Font.BOLD, 4 * fontSize / 5);
			composant.setFont(fontText);
			
			composant.setLocation(widthComposant, heightComposant);
			combo.setPreferredSize(new Dimension(width/4, height/8));
			combo.setLocation(widthComposant +  width / 2, heightComposant);
			
			supprimer.setLocation(widthComposant, heightComposant + height/3);
			
			this.updateUI();
		}
		
		//accesseurs
		public Hashtable<String, ArrayList<String>> getTable() {
			return table;
		}
		
		public void setTable(Hashtable<String, ArrayList<String>> table) {
			this.table = table;
		}
		
		public JComboBox getCombo() {
			return combo;
		}
		
		public void setCombo(JComboBox combo) {
			this.combo = combo;
		}
		
		public String[] getKeys() throws NumberFormatException, IOException {
			Hashtable<String, ArrayList<String>> t = Controller.readConfigFile();
			String[] res = new String[t.size()];
			int i = 0;
			Set<String> keys = table.keySet();
			for (Entry<String, ArrayList<String>> mapentry : table.entrySet()) {
				res[i] = mapentry.getKey();
				i++;
			}
			return res;
		}
}
