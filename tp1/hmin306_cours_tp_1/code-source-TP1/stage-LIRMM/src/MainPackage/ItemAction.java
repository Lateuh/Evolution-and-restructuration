package MainPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

class ItemAction implements ActionListener{
	private JComboBox<?> combo;
	
	public ItemAction(JComboBox<?> combo) {
		this.combo = combo;
	}
	
    public void actionPerformed(ActionEvent e) {
    	combo.updateUI();
    	combo.getSelectedItem();
    }               
  }