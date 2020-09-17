package MainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

//panneau list des composants dans onglet configuration
public class PanneauListeComposants extends Panneau  {
		//attributs 
		private Hashtable<String, ArrayList<String>> table;
		private JPanel panel;
		private Color color;
		private ArrayList<String> labels;
		private JList<String> listArea;
		private JScrollPane listScroller;
		
		//constructeur
		public PanneauListeComposants(Color c) throws NumberFormatException, IOException {
			super(c);
			
			color = c;
			 
			table = Controller.readConfigFile();
			 
			panel = new JPanel(new BorderLayout());
			
            labels = new ArrayList<String>(table.keySet());
           
			//trie la liste
			Collections.sort(labels);
			
            listArea = new JList<String>(labels.toArray(new String[labels.size()]));
         
            //couleurs
            listArea.setBackground(c);
            listArea.setForeground(Color.WHITE);
            
            listArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listArea.setFont(new Font("Arial", Font.ITALIC, 24));
            listScroller = new JScrollPane();
            listScroller.setViewportView(listArea);
            listArea.setLayoutOrientation(JList.VERTICAL);
            panel.add(listScroller);
            
            this.add(panel);
		}
		
		//affichage du panneau
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			//tailles du panneau
			int width = this.getWidth();
			int height = this.getHeight();
			
			//nom panneau
			g.setColor(Color.WHITE);
			String listeComposants = "Liste des composants";
			Font font = new Font("Arial", Font.BOLD, width/50);
			g.setFont(font);
			int xList =  3 * listeComposants.length() / 5;
			int yList = listeComposants.length() + 5;
			g.drawString(listeComposants, xList, yList);
			
			
			panel.setPreferredSize(new Dimension(width, height - 3 * yList / 2));
			panel.setLocation(0, 3 * yList / 2);
			
			this.updateUI();
		}
		
		//modifie affichage � chaque suppression ou ajout 
		public void update() throws NumberFormatException, IOException {
			
			Hashtable<String, ArrayList<String>> table = Controller.readConfigFile();
			labels = new ArrayList<String>(table.keySet());
			
			Collections.sort(labels);
			
			listArea = new JList<String>(labels.toArray(new String[labels.size()]));
			
			//couleurs
			listArea.setBackground(color);
            listArea.setForeground(Color.WHITE);
			
			listArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listArea.setFont(new Font("Arial", Font.ITALIC, 24));
			listScroller.setViewportView(listArea);
			listArea.setLayoutOrientation(JList.VERTICAL);
            
            this.updateUI();
		}
		
		//accesseurs
		public ArrayList<String> getLabels() {
			return labels;
		}
		
		public void setLabels(ArrayList<String> labels) {
			this.labels = labels;
		}
		
		public JList<String> getListArea() {
			return listArea;
		}
		
		public void setListArea(JList<String> listArea) {
			this.listArea = listArea;
		}
		
		public JScrollPane getListScroller() {
			return listScroller;
		}
		
		public void setListScroller(JScrollPane listScroller) {
			this.listScroller = listScroller;
		}
}
