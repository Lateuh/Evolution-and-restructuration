package MainPackage;

import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class PopUpDemo extends JPopupMenu {
	private static final long serialVersionUID = 1L;
		
    public PopUpDemo(PanneauTempsReel component, ArrayList<String> liste) {
    	for (int i = 0; i< liste.size(); i++) {
    		JMenuItem anItem;
    		String champ = liste.get(i);
    		anItem = new JMenuItem(champ);
            anItem.addActionListener(event -> component.setDisplayKey(champ));
            add(anItem);
    	}      
    }
}
