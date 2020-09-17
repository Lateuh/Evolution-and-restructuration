package MainPackage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;

public class ButtonSuppression extends JButton implements MouseListener  {
	
	//attributs
	private JComboBox<String> combo;
	private Hashtable<String, ArrayList<String>> table;
	private PanneauListeComposants panCompo;
	
	public ButtonSuppression(String str, JComboBox<String> combo, Hashtable<String, ArrayList<String>> table, PanneauListeComposants panCompo) {
		super(str);
		this.combo = combo;
		this.table = table;
		this.panCompo = panCompo;
		this.addMouseListener(this);
	}
	
	//quand on clic sur le bouton 
	public void mouseClicked(MouseEvent arg0) {
		if(combo.getItemCount() > 0) {
			//recuperation de table pour les eventuels ajouts
			try {
				table = Controller.readConfigFile();
			} catch (NumberFormatException | IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			table.remove(combo.getSelectedItem().toString());
			combo.removeItem(combo.getSelectedItem());
			try {
				Controller.writeConfigFile(table);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//update liste
			try {
				panCompo.update();
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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