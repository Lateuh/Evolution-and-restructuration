package MainPackage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class ButtonAjout extends JButton implements MouseListener  {
	
	//attributs
	private JTextField nom;
	private JTextField conso;
	private JTextField constructeur;
	private JComboBox typeComposant;
	private JComboBox MiseEnCirculation;
	
	private Hashtable<String, ArrayList<String>> table;
	
	private PanneauListeComposants panCompo;
	private PanneauSuppression panSup;
	
	//constructeur
	public ButtonAjout(String str, JTextField nom, JTextField conso, JTextField constructeur, JComboBox typeComposant, JComboBox MiseEnCirculation, Hashtable<String, ArrayList<String>> table, PanneauSuppression panSup, PanneauListeComposants panCompo) {
		super(str);
		
		this.nom = nom;
		this.conso = conso;
		this.constructeur = constructeur;
		this.typeComposant = typeComposant;
		this.MiseEnCirculation = MiseEnCirculation;
		
		this.table = table;
		this.panSup = panSup;
		this.panCompo = panCompo;
		
		this.addMouseListener(this);
	}

	//quand on clic sur le bouton 
	public void mouseClicked(MouseEvent arg0) {
		nom.updateUI();
		conso.updateUI();
		constructeur.updateUI();
		
		if (!nom.getText().equals("") && !conso.getText().equals("") && !constructeur.getText().equals("") && estUnEntier(conso.getText())) {
			//mise a jour de la table si suppression
			try {
				table = Controller.readConfigFile();
			} catch (NumberFormatException | IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			//ajout le nouveau composant
			ArrayList<String> list = new ArrayList<String>();
			list.add(String.valueOf(conso.getText()));
			list.add(String.valueOf(constructeur.getText()));
			list.add(String.valueOf(typeComposant.getSelectedItem()));
			list.add(String.valueOf(MiseEnCirculation.getSelectedItem()));
			table.put(nom.getText(), list);
			
			//on ecrit la liste des composants dans le fichier
			try {
				Controller.writeConfigFile(table);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//rempli la table du panneau suppression
			if (panSup.getCombo().countComponents() > 0) {
				panSup.getCombo().removeAllItems();
			}
			
			//mise a jour du combo
			Set<String> keys = table.keySet();
			for (Entry<String, ArrayList<String>> mapentry : table.entrySet()) {
				panSup.getCombo().addItem(mapentry.getKey());
			}
			
			//update liste
			try {
				panCompo.update();
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//clear le texte
			nom.setText("");
			constructeur.setText("");
		}
		conso.setText("");
	}
	
	public boolean estUnEntier(String chaine) {
		try {
			Double.parseDouble(chaine);
		} catch (NumberFormatException e){
			return false;
		}
 
		return true;
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
}